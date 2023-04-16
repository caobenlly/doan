package com.example.doantotnghiep.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "refesh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column( unique = true,name = "token" )
    private String token;

    @Column(name ="expiry_date")
    private Date expiryDate;
}
