package com.example.doantotnghiep.service.impl;

import com.example.doantotnghiep.entity.Image;
import com.example.doantotnghiep.exception.BadRequestException;
import com.example.doantotnghiep.exception.InternalServerException;
import com.example.doantotnghiep.exception.NotFoundException;
import com.example.doantotnghiep.responsitory.ImageRepository;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImageServiceImpl implements ImageService {
    private static String UPLOAD_DIR = System.getProperty("user.home") + "/media/upload";
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    @Override
    @Transactional(rollbackFor = InternalServerException.class)
    public void deleteImage(String uploadDir, String filename) {

        //Lấy đường dẫn file
        String link = "/media/static/" + filename;
        //Kiểm tra link
        Image image = imageRepository.findByLink(link);
        if (image == null) {
            throw new BadRequestException("File không tồn tại");
        }

        //Kiểm tra ảnh đã được dùng
        Integer inUse = imageRepository.checkImageInUse(link);
        if (inUse != null) {
            throw new BadRequestException("Ảnh đã được sử dụng không thể xóa!");
        }

        //Xóa file trong databases
        imageRepository.delete(image);

        //Kiểm tra file có tồn tại trong thư mục
        File file = new File(uploadDir + "/" + filename);
        if (file.exists()) {
            //Xóa file ở thư mục
            if (!file.delete()) {
                throw new InternalServerException("Lỗi khi xóa ảnh!");
            }
        }
    }

    @Override
    public List<String> getListImageOfUser(long userId) {
        return imageRepository.getListImageOfUser(userId);
    }



    @Override
    public String uploadFile1( MultipartFile file) {
        //Tạo thư mục chứa ảnh nếu không tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        //Lấy tên file và đuôi mở rộng của file
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (originalFilename.length() > 0) {

            //Kiểm tra xem file có đúng định dạng không
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("gif") && !extension.equals("svg") && !extension.equals("jpeg")) {
                throw new BadRequestException("Không hỗ trợ định dạng file này!");
            }
            try {
                Image image = new Image();
                image.setId(UUID.randomUUID().toString());
                image.setName(file.getName());
                image.setSize(file.getSize());
                image.setType(extension);
                String link =  image.getId() + "." + extension;
                image.setLink(link);
                image.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                image.setCreatedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser());

                //Tạo file
                File serveFile = new File(UPLOAD_DIR + "/" + image.getId() + "." + extension);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(serveFile));
                bos.write(file.getBytes());
                bos.close();

                saveImage(image);
                List<String> a = new ArrayList<>();

                return link;

            } catch (Exception e) {
                throw new InternalServerException("Có lỗi trong quá trình upload file!");
            }
        }
        throw new BadRequestException("File không hợp lệ!");
    }
    @Override
    public UrlResource downloadFile1(String filename) {
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()) {
            throw new NotFoundException("File không tồn tại!");
        }

        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException ex) {
            throw new NotFoundException("File không tồn tại!");
        }

//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
//                .body(resource);
        return  resource;
    }
}
