package com.avdhoot.StudyGroupFinderAPI.dto.userDto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 70, message = "Name must be between 2 and 70 characters")
        String name,

        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 70, message = "Username must be between 2 and 70 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
        String password
){
}
