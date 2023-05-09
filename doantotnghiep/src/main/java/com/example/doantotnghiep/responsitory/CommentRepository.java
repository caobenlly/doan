package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.Comment;
import com.example.doantotnghiep.model.dto.OrderInfoDTO;
import com.example.doantotnghiep.model.responeadmin.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentByProductId(String id);

    @Query( value = "select new com.example.doantotnghiep.model.responeadmin.CommentResponse(o) from Comment o where o.product.id= :id")
    List<CommentResponse> getListComment(@Param("id") String  id);
}
