package com.aliexpress.usersservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class BuyerRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private String address;
    private String hashedPassword;

    // getters and setters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getAddress() {
        return address;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
