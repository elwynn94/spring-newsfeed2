package com.sparta.springnewsfeed.user;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.auth.WithdrawRequestDto;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.user.dto.SignupRequestDto;
import com.sparta.springnewsfeed.user.dto.UpdatePasswordRequestDto;
import com.sparta.springnewsfeed.user.dto.UpdateProfileRequestDto;
import com.sparta.springnewsfeed.user.dto.UserInquiryResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup() {
        // Given: Fixture Monkey를 사용하여 SignupRequestDto 데이터 생성
        SignupRequestDto requestDto = fixtureMonkey.giveMeOne(SignupRequestDto.class);
        when(userService.signup(requestDto)).thenReturn(ResponseCode.CREATED);

        // When: signup 메소드 호출
        ResponseEntity<HttpStatusResponseDto> response = userController.signup(requestDto);

        // Then: 응답 코드 검증
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdateProfile() {
        // Given: Fixture Monkey를 사용하여 UpdateProfileRequestDto 데이터 생성
        UpdateProfileRequestDto requestDto = fixtureMonkey.giveMeOne(UpdateProfileRequestDto.class);
        MultipartFile mockFile = mock(MultipartFile.class);
        UserDetailsImpl userDetails = fixtureMonkey.giveMeOne(UserDetailsImpl.class);
        when(userService.updateProfile(requestDto, mockFile, userDetails.getUser().getUserId())).thenReturn(ResponseCode.SUCCESS);

        // When: updateProfile 메소드 호출
        ResponseEntity<HttpStatusResponseDto> response = userController.updateProfile(requestDto, mockFile, userDetails);

        // Then: 응답 코드 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePassword() {
        // Given: Fixture Monkey를 사용하여 UpdatePasswordRequestDto 데이터 생성
        UpdatePasswordRequestDto requestDto = fixtureMonkey.giveMeOne(UpdatePasswordRequestDto.class);
        UserDetailsImpl userDetails = fixtureMonkey.giveMeOne(UserDetailsImpl.class);
        when(userService.updatePassword(requestDto, userDetails.getUser().getUserId())).thenReturn(ResponseCode.SUCCESS);

        // When: updatePassword 메소드 호출
        ResponseEntity<HttpStatusResponseDto> response = userController.updatePassword(requestDto, userDetails);

        // Then: 응답 코드 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetUserProfile() {
        // Given: Fixture Monkey를 사용하여 UserInquiryResponseDto 데이터 생성
        UserDetailsImpl userDetails = fixtureMonkey.giveMeOne(UserDetailsImpl.class);
        UserInquiryResponseDto userProfile = fixtureMonkey.giveMeOne(UserInquiryResponseDto.class);
        when(userService.getUserProfile(userDetails.getUser().getUserId())).thenReturn(userProfile);

        // When: getUserProfile 메소드 호출
        ResponseEntity<UserInquiryResponseDto> response = userController.getUserProfile(userDetails);

        // Then: 응답 코드 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userProfile, response.getBody());
    }
}