package com.portifolio.wealthinker.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserFormDTO {
    @NotBlank(message="Name is required")
    @Size(min=3, message="Min 3 character required")
    @Size(max=20, message="Maimum 20 characted only needed")
    private String name;

    @NotBlank(message="Username is required")
    @Size(min=3, message="Min 3 character required")
    @Size(max=20, message="Maimum 20 characted only needed")
    private String username;

    @Email(message="Invalid Email Address")
    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=4, max=15)
    private String password;

    @NotBlank(message="About is required")
    private String about;
    
    @NotBlank(message="Phone Number is required")
    // @Pattern(regexp="^[0-9]{10}", message="Invalid Phone Number")
    private String phoneNumber;
}

