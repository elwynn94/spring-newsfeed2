package com.sparta.springnewsfeed.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.auth.WithdrawRequestDto;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.user.dto.SignupRequestDto;
import com.sparta.springnewsfeed.user.dto.UpdatePasswordRequestDto;
import com.sparta.springnewsfeed.user.dto.UpdateProfileRequestDto;
import com.sparta.springnewsfeed.user.dto.UserInquiryResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository=userRepository;
    }


    // 공통 응답 처리 Generic 메서드
    private <T> ResponseEntity<HttpStatusResponseDto> createResponse(ResponseCode responseCode) {
        HttpStatusResponseDto response = new HttpStatusResponseDto(responseCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(responseCode.getStatusCode()));
    }

    private <T> ResponseEntity<T> createResponse(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatusResponseDto> signup(@Validated @RequestBody SignupRequestDto requestDto) {
        ResponseCode responseCode = userService.signup(requestDto);
        return createResponse(responseCode);
    }

    // 프로필 수정
    @PutMapping("/update-profile")
    public ResponseEntity<HttpStatusResponseDto> updateProfile(
            @Validated @RequestPart("updateProfileRequestDto") UpdateProfileRequestDto requestDto,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userId = userDetails.getUser().getUserId();
        ResponseCode responseCode = userService.updateProfile(requestDto, profilePicture, userId);
        return createResponse(responseCode);
    }

    // 비밀번호 변경
    @PutMapping("/update-password")
    public ResponseEntity<HttpStatusResponseDto> updatePassword(
            @Validated @RequestBody UpdatePasswordRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userId = userDetails.getUser().getUserId();
        ResponseCode responseCode = userService.updatePassword(requestDto, userId);
        return createResponse(responseCode);
    }

    // 사용자 프로필 조회
    @GetMapping("/inquiry")
    public ResponseEntity<UserInquiryResponseDto> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userId = userDetails.getUser().getUserId();
        UserInquiryResponseDto userProfile = userService.getUserProfile(userId);
        return createResponse(userProfile, HttpStatus.OK);
    }

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpStatusResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        HttpStatusResponseDto response = new HttpStatusResponseDto(ResponseCode.INVALID_INPUT_VALUE);
        return createResponse(response, HttpStatus.BAD_REQUEST);
    }

    // 일반 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpStatusResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseCode responseCode;
        if (ex.getMessage().equals(ResponseCode.DUPLICATE_ENTITY.getMessage())) {
            responseCode = ResponseCode.DUPLICATE_ENTITY;
        } else if (ex.getMessage().equals(ResponseCode.INVALID_INPUT_VALUE.getMessage())) {
            responseCode = ResponseCode.INVALID_INPUT_VALUE;
        } else {
            responseCode = ResponseCode.INTERNAL_SERVER_ERROR;
        }
        return createResponse(responseCode);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatusResponseDto> handleException(Exception ex) {
        ex.printStackTrace();
        return createResponse(ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 헤더에서 토큰을 가져옴 ->
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        // 토큰이 유효한지 확인
        if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
            Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
            String userId = refreshTokenClaims.getSubject();

            // 유저의 리프레시 토큰 삭제
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                user.setRefreshToken(null);
                userRepository.save(user);
            }
        }

        // 클라이언트 측 토큰 삭제 요청을 위해 응답 설정
        return createResponse("Logged out successfully", HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawUser(HttpServletRequest request, @RequestBody WithdrawRequestDto requestDto) {
        // 헤더에서 토큰을 가져옴
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        // 토큰이 유효한지 확인 (여기서는 accessToken을 사용하여 유효성 검증)
        if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
            Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
            String userId = accessTokenClaims.getSubject();

            // 유저 정보 가져오기
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user == null) {
                return createResponse("User not found", HttpStatus.NOT_FOUND);
            }

            // 비밀번호가 일치하는지 확인
            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                return createResponse("Incorrect password", HttpStatus.UNAUTHORIZED);
            }

            // 리프레시 토큰도 유효한지 확인
            if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
                // 유저의 리프레시 토큰 삭제, 유저의 상태변경
                user.setRefreshToken(null);
                user.setStatus(UserStatusEnum.DELETED);
                userRepository.save(user);

                // 클라이언트 측 토큰 삭제 요청을 위해 응답 설정
                return createResponse("User deleted successfully", HttpStatus.OK);
            } else {
                return createResponse("Invalid refresh token", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return createResponse("Invalid access token", HttpStatus.UNAUTHORIZED);
        }
    }
}
