package com.example.doantotnghiep.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "`Reset_Password_Token`")
public class ResetPasswordToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "otp",unique = true)
	private Integer otp;
	@Column(name = "token",unique = true)
	private String token;
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "`expiry_date`", nullable = false)
	private Date expiryDate;

	public ResetPasswordToken() {
	}

	public ResetPasswordToken(Integer otp, User user, String token) {
		this.token = token;
		this.user = user;
		this.otp = otp;

		// 1h
		expiryDate = new Date(System.currentTimeMillis() + 360000);
	}



}
