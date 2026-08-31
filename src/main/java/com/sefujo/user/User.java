package com.sefujo.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String createdDate;
    private String updatedDate;

}
