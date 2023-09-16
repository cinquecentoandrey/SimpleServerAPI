package com.cinquecento.simpleserverapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "Users")
@Component("User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 16, message = "Username length should be between 2 and 16 symbols")
    private String username;

    @Column(name = "age")
    @Min(value = 14, message = "Age should be greater than zero")
    @Max(value = 120, message = "Age should be less than 120")
    private Integer age;

    @Column(name = "telephone")
    @NotEmpty(message = "Field should not be empty")
    @Pattern(regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
            message = "Correct format: +79855310868 | +7 (926) 777-77-77 | 89855310868")
    private String telephone;

    @Column(name = "email")
    @Email
    @NotEmpty(message = "Email should not not be empty")
    private String email;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "image_uri")
    private String imageURI;

    public User() {
    }

    public User(Long id, String username, Integer age, String telephone, String email, Status status, String imageURI) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.telephone = telephone;
        this.email = email;
        this.status = status;
        this.imageURI = imageURI;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
