package com.aliexpress.authenticationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private String address;
    private String password;


}
