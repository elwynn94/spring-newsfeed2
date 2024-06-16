package com.sparta.springnewsfeed.user.dto;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestDtoTest {

    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    public void testSignupRequestDtoCreation() {
        // Given: Fixture Monkey를 사용하여 SignupRequestDto 데이터 생성
        SignupRequestDto signupRequestDto = fixtureMonkey.giveMeOne(SignupRequestDto.class);

        // When & Then: 필드들이 null이 아닌지 확인
        assertNotNull(signupRequestDto.getUserId());
        assertNotNull(signupRequestDto.getPassword());
        assertNotNull(signupRequestDto.getEmail());
    }

    @Test
    public void testSignupRequestDtoCustomData() {
        // Given: 특정 조건을 가진 SignupRequestDto 데이터 생성
        SignupRequestDto signupRequestDto = fixtureMonkey.giveMeBuilder(SignupRequestDto.class)
                .set("userId", "testUser1234")
                .set("password", "TestPass1234!")
                .set("email", "testuser@example.com")
                .sample();

        // When & Then: 필드 값이 예상한 대로 설정되었는지 확인
        assertEquals("testUser1234", signupRequestDto.getUserId());
        assertEquals("TestPass1234!", signupRequestDto.getPassword());
        assertEquals("testuser@example.com", signupRequestDto.getEmail());
    }

    @Test
    public void testSignupRequestDtoEqualsAndHashCode() {
        // Given: 동일한 필드 값을 가진 두 개의 SignupRequestDto 생성
        SignupRequestDto dto1 = fixtureMonkey.giveMeBuilder(SignupRequestDto.class)
                .set("userId", "testUser1234")
                .set("password", "TestPass1234!")
                .set("email", "testuser@example.com")
                .sample();
        SignupRequestDto dto2 = fixtureMonkey.giveMeBuilder(SignupRequestDto.class)
                .set("userId", "testUser1234")
                .set("password", "TestPass1234!")
                .set("email", "testuser@example.com")
                .sample();

        // When & Then: equals 및 hashCode 검증
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testSignupRequestDtoToString() {
        // Given: 특정 조건을 가진 SignupRequestDto 데이터 생성
        SignupRequestDto signupRequestDto = fixtureMonkey.giveMeBuilder(SignupRequestDto.class)
                .set("userId", "testUser1234")
                .set("password", "TestPass1234!")
                .set("email", "testuser@example.com")
                .sample();

        // When: toString 호출
        String toString = signupRequestDto.toString();

        // Then: toString 메소드가 올바른 문자열을 포함하는지 확인
        assertTrue(toString.contains("userId=testUser1234"));
        assertTrue(toString.contains("password=TestPass1234!"));
        assertTrue(toString.contains("email=testuser@example.com"));
    }
}