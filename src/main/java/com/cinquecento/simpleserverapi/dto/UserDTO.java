package com.cinquecento.simpleserverapi.dto;

import com.cinquecento.simpleserverapi.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public class UserDTO {

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 16, message = "Username length should be between 2 and 16 symbols")
    private String username;

    @Min(value = 14, message = "Age should be greater than zero")
    @Max(value = 120, message = "Age should be less than 120")
    private Integer age;

    @NotEmpty(message = "Field should not be empty")
    @Pattern(regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$",
            message = "Correct format: +79855310868 | +7 (926) 777-77-77 | 89855310868")
    private String telephone;

    @Email
    @NotEmpty(message = "Email should not not be empty")
    private String email;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

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
}
