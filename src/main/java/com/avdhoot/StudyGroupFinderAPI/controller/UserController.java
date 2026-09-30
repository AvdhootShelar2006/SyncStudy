package com.avdhoot.StudyGroupFinderAPI.controller;

import com.avdhoot.StudyGroupFinderAPI.dto.loginDto.LoginRequestDto;
import com.avdhoot.StudyGroupFinderAPI.dto.loginDto.LoginResponseDto;
import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserDetailResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.userDto.CreateUserRequestDto;
import com.avdhoot.StudyGroupFinderAPI.service.JwtService;
import com.avdhoot.StudyGroupFinderAPI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    /*
        Authentication Endpoints
     */

    // Register
    @PostMapping("/user/register")
    public ResponseEntity<CreateUserDetailResponse> registerMember(
            @Valid @RequestBody CreateUserRequestDto requestDto
    ) {
        CreateUserDetailResponse response = userService.registerUser(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Login
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                requestDto.username(),
                requestDto.password()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationRequest);

        String token = jwtService.generateToken(authentication);

        LoginResponseDto response = new LoginResponseDto(token);
        return ResponseEntity.ok(response);
    }




    @GetMapping("/user/{userId}")
    public ResponseEntity<CreateUserDetailResponse> getUserById(
            @PathVariable int userId
    ){
        return ResponseEntity.ok(userService.getMemberById(userId));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<CreateUserDetailResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<CreateUserDetailResponse> createMemberDetailResponses = userService.getAllMembers(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createMemberDetailResponses);
    }
}
