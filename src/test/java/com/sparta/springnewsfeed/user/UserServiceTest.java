package com.sparta.springnewsfeed.user;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.common.S3Uploader;
import com.sparta.springnewsfeed.user.dto.SignupRequestDto;
import com.sparta.springnewsfeed.user.dto.UpdateProfileRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private UserService userService;

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
        SignupRequestDto requestDto = fixtureMonkey.giveMeBuilder(SignupRequestDto.class)
                .set("userId", "testUser123")
                .set("password", "TestPass123!")
                .set("email", "test@example.com")
                .sample();

        when(userRepository.existsByUserId(requestDto.getUserId())).thenReturn(false);
        when(userRepository.existsByUserIdAndStatus(requestDto.getUserId(), UserStatusEnum.DELETED)).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        // When: signup 메소드 호출
        ResponseCode response = userService.signup(requestDto);

        // Then: 사용자 저장 확인 및 응답 코드 검증
        assertEquals(ResponseCode.CREATED, response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testUpdateProfile() throws IOException {
        // Given: Fixture Monkey를 사용하여 UpdateProfileRequestDto 데이터 생성
        UpdateProfileRequestDto requestDto = fixtureMonkey.giveMeBuilder(UpdateProfileRequestDto.class)
                .set("name", "newName")
                .set("introduction", "newIntroduction")
                .sample();
        MultipartFile mockFile = mock(MultipartFile.class);

        User user = fixtureMonkey.giveMeBuilder(User.class)
                .set("userId", "testUser123")
                .setNull("name")
                .setNull("introduction")
                .setNull("pictureURL")
                .sample();

        when(userRepository.findByUserId("testUser123"))
                .thenReturn(Optional.of(user));
        when(s3Uploader.upload(any(MultipartFile.class),
                eq("profile-pictures")))
                .thenReturn("http://example.com/newProfile.jpg");

        // When: updateProfile 메소드 호출
        ResponseCode response = userService.updateProfile(requestDto, mockFile, "testUser123");

        // Then: 사용자 저장 확인 및 필드 값 검증
        assertEquals(ResponseCode.SUCCESS, response);
        verify(userRepository).save(user);
        assertEquals(requestDto.getName(), user.getName());
        assertEquals(requestDto.getIntroduction(), user.getIntroduction());
        assertEquals("http://example.com/newProfile.jpg", user.getPictureURL());
    }


}