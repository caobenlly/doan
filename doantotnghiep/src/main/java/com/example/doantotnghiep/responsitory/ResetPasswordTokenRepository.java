package com.example.doantotnghiep.responsitory;




import com.example.doantotnghiep.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {

	public ResetPasswordToken findByToken(String token);

	public boolean existsByToken(int token);

	@Query("	SELECT 	token	"
			+ "	FROM 	ResetPasswordToken "
			+ " WHERE 	user.id = :userId")
	public String findByUserId(@Param("userId") int userId);

	@Transactional
	@Modifying
	@Query("	DELETE 						"
			+ "	FROM 	ResetPasswordToken 	"
			+ " WHERE 	user.id = :userId")
	public void deleteByUserId(@Param("userId") int userId);


	@Query("	SELECT 	otp	"
			+ "	FROM 	ResetPasswordToken "
			+ " WHERE 	user.id = :userId")
	public int findByOtp(@Param("userId") int userId);
}
