package com.example.doantotnghiep.responsitory;





import com.example.doantotnghiep.entity.RegistrationUserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface RegistrationUserTokenRepository extends JpaRepository<RegistrationUserToken, Integer> {

	public RegistrationUserToken findByToken(String token);

	public boolean existsByToken(String token);
//	@Modifying
//	@Query("	SELECT 	token	"
//			+ "	FROM 	RegistrationUserToken "
//			+ " WHERE 	user.id = :userId")
	public RegistrationUserToken findFirstByUserId(@Param("userId") int userId);

	@Transactional
	@Modifying
	@Query("	DELETE 							"
			+ "	FROM 	RegistrationUserToken 	"
			+ " WHERE 	user.id = :userId")
	public void deleteByUserId(@Param("userId") int userId);

}
