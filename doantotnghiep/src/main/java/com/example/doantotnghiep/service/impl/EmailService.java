package com.example.doantotnghiep.service.impl;


import com.example.doantotnghiep.entity.RegistrationUserToken;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.responsitory.RegistrationUserTokenRepository;
import com.example.doantotnghiep.responsitory.ResetPasswordTokenRepository;
import com.example.doantotnghiep.service.IEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RegistrationUserTokenRepository registrationUserTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    /*
     * @see
     * com.vti.service.IEmailService#sendRegistrationUserConfirm(java.lang.String)
     */
    @Override
    public void sendRegistrationUserConfirm(String email) {

        User user = userService.findUserByEmail(email);
        RegistrationUserToken tokenEntity= registrationUserTokenRepository.findFirstByUserId(user.getId());
        String token = tokenEntity.getToken();
        String confirmationUrl = "http://192.168.1.34:8080/#/email-verify";
        String Name = user.getEmail();
        String subject = "Xác Nhận Đăng Ký Account";
        String content = "<html>" +
                "<body style=\"display: flex; height: 100vh;\">\n" +
                "        <div style=\"width: 830px; height: 80%; text-align: center; background-color: white; margin: auto; border-radius: 20px; border: 5px solid #3b82f6;\">\n" +
                "            <img style=\"width: 400px; margin-top: 5%;\" src='cid:identifier1234'>\n" +
                "            <h1 style=\"margin: 5%;\">BLUCECO GLOBAL TODO APP REGISTER</h1>\n" +
                "            <div style=\"width: 60%; height: 50%; margin: auto; background-color: white; border: 1px solid black; border-radius: 20px;\">\n" +
                "                <h1>Hi " +Name +" ,</h1>\n" +
                "                <p>You have just registered a TODO APP account of BLUECO-GLOBAL company. </br>\n" +
                "                    To verify your account, please click the button below.</p>\n" +
                "                <a style=\"background-color: #3b82f6; padding: 10px; border: 1px solid gray; text-decoration: none; color: white;\" href=\"" + confirmationUrl + "\">Click link please!!!</a>\n" +
                "                <p>If you do not fulfill this request please ignore it or if you need assistance contact us immediately.</p>\n" +
                "                <p>Best Regards, <br>\n" +
                "                    Blueco Global Corp.</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "</body>" +
                "</html>";


//        sendEmail(email, subject, content);
        sendMailLinkVerify(email, subject,confirmationUrl,Name);
    }

    @Override
    public void sendResetPassword(String email) {

        User user = userService.findUserByEmail(email);
        int tokendb = resetPasswordTokenRepository.findByOtp( user.getId());
//        int token = tokendb.getToken();
        String Name = user.getEmail();
//        String confirmationUrl = "http://localhost:3000/auth/new-password/" + token;
//
        String subject = "Thiết lập lại mật khẩu";
//        String content = "Click vào link dưới đây để thiết lập lại mật khẩu (nếu không phải bạn xin vui lòng bỏ qua).\n"
//                + confirmationUrl;

//        sendEmail(email, subject, content);
        sendMailOTP(email,subject, tokendb,Name);
    }

    private void sendEmail(final String recipientEmail, final String subject, final String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    public void sendMailLinkVerify(String to, String subject, String url, String Name) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
                mimeMessage.setSubject(subject);

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                String body = "<html>" +
                        "<body style=\"display: flex; height: 100vh;\">\n" +
                        "        <div style=\"width: 830px; height: 80%; text-align: center; background-color: white; margin: auto; border-radius: 20px; border: 5px solid #3b82f6;\">\n" +
                        "            <img style=\"width: 400px; margin-top: 5%;\" src='cid:identifier1234'>\n" +
                        "            <h1 style=\"margin: 5%;\">HUY HUNG SHOP APP REGISTER</h1>\n" +
                        "            <div style=\"width: 60%; height: 50%; margin: auto; background-color: white; border: 1px solid black; border-radius: 20px;\">\n" +
                        "                <h1>Hi  "+Name +",</h1>\n" +
                        "                <p>You have just registered a TODO APP account of Shop Shoes. </br>\n" +
                        "                    To verify your account, please click the button below.</p>\n" +
                        "                <a style=\"background-color: #3b82f6; padding: 10px; border: 1px solid gray; text-decoration: none; color: white;\" href=\"" + url + "\">Click link please!!!</a>\n" +
                        "                <p>If you do not fulfill this request please ignore it or if you need assistance contact us immediately.</p>\n" +
                        "                <p>Best Regards, <br>\n" +
                        "                    Shoes Shop.</p>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "</body>" +
                        "</html>";

                helper.setText(body, true);

                FileSystemResource res = new FileSystemResource(new File("logo.png"));
                helper.addInline("identifier1234", res);
            }
        };

        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }


    public void sendMailOTP(String to, String subject, int otp,String Name) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
                mimeMessage.setSubject(subject);

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                String body = "<html>" +
                        "<body style=\"display: flex; height: 900px;\">\n" +
                        "        <div style=\"width: 830px; height: 80%; text-align: center; background-color: white; margin: auto; border-radius: 20px; border: 5px solid #3b82f6;\">\n" +
                        "            <img style=\"width: 400px; margin-top: 5%;\" src='cid:identifier1234'>\n" +
                        "            <h1 style=\"margin: 5%;\">HUY HUNG SHOP TODO APP OTP</h1>\n" +
                        "            <div>\n" +
                        "                <div style=\"width: 60%; height: 50%; margin: auto; background-color: white; border: 1px solid black; border-radius: 20px;\">\n" +
                        "                    <h1>Hi "+Name +",</h1>\n" +
                        "                    <p>You have just received a confirmation OTP at HUY HUNG SHOP:</p>\n" +
                        "                    <span style=\"border: 1px solid black; font-size: 40px; color: #3b82f6; border-radius: 3px; padding: 5px;\">\n" +
                        otp +
                        "                        \n" +
                        "                    </span>\n" +
                        "                    <p>If you do not fulfill this request please ignore it or if you need assistance contact us immediately.</p>\n" +
                        "                    <p>Best Regards, <br>\n" +
                        "                        HUY HUNG SHOP.</p>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "</body>" +
                        "</html>";

                helper.setText(body, true);

                FileSystemResource res = new FileSystemResource(new File("logo.gif"));
                helper.addInline("identifier1234", res);
            }
        };

        try {
            mailSender.send(preparator);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
