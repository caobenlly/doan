package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.Post;
import com.example.doantotnghiep.model.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(nativeQuery = true, name = "adminGetListPost")
    List<PostDTO> adminGetListPost(@Param("title") String title, @Param("status") String status, @Param("limit") int limit, @Param("offset") int offset);

    @Query(nativeQuery = true, value = "SELECT count(id) " +
            "FROM post " +
            "WHERE title LIKE CONCAT('%',:title,'%') AND status LIKE CONCAT('%',:status,'%') ")
    int countPostFilter(@Param("title") String title, @Param("status") String status);

    @Query(value = "SELECT new com.example.doantotnghiep.model.dto.PostDTO(p) FROM Post p ")
    List<PostDTO> adminGetListPosts();

    @Query(nativeQuery = true, value = "SELECT * FROM post p WHERE p.status = ?1 ORDER BY p.published_at DESC LIMIT ?2")
    List<Post> getLatesPosts(int status, int limit);

    List<Post> findAllByStatus(int status);

    @Query(nativeQuery = true, value = "SELECT * FROM post WHERE status = ?1 AND id != ?2 ORDER BY published_at DESC LIMIT ?3")
    List<Post> getLatestPostsNotId(int publicStatus, long id, int limit);
}
