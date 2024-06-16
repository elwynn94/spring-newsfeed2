package com.sparta.springnewsfeed.user.dto;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePasswordRequestDtoTest {

    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    public void testUpdatePasswordRequestDtoCustomData() {
        // Given: 특정 조건을 가진 UpdatePasswordRequestDto 데이터 생성
        UpdatePasswordRequestDto updatePasswordRequestDto = fixtureMonkey.giveMeBuilder(UpdatePasswordRequestDto.class)
                .set("currentPassword", "CurrentPass123!")
                .set("newPassword", "NewPass1234!")
                .sample();

        // When & Then: 필드 값이 예상한 대로 설정되었는지 확인
        assertEquals("CurrentPass123!", updatePasswordRequestDto.getCurrentPassword());
        assertEquals("NewPass1234!", updatePasswordRequestDto.getNewPassword());
    }

    @Test
    public void testUpdatePasswordRequestDtoEqualsAndHashCode() {
        // Given: 동일한 필드 값을 가진 두 개의 UpdatePasswordRequestDto 생성
        UpdatePasswordRequestDto dto1 = fixtureMonkey.giveMeBuilder(UpdatePasswordRequestDto.class)
                .set("currentPassword", "CurrentPass123!")
                .set("newPassword", "NewPass1234!")
                .sample();
        UpdatePasswordRequestDto dto2 = fixtureMonkey.giveMeBuilder(UpdatePasswordRequestDto.class)
                .set("currentPassword", "CurrentPass123!")
                .set("newPassword", "NewPass1234!")
                .sample();

        // When & Then: equals 및 hashCode 검증
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testUpdatePasswordRequestDtoToString() {
        // Given: 특정 조건을 가진 UpdatePasswordRequestDto 데이터 생성
        UpdatePasswordRequestDto updatePasswordRequestDto = fixtureMonkey.giveMeBuilder(UpdatePasswordRequestDto.class)
                .set("currentPassword", "CurrentPass123!")
                .set("newPassword", "NewPass1234!")
                .sample();

        // When: toString 호출
        String toString = updatePasswordRequestDto.toString();

        // Then: toString 메소드가 올바른 문자열을 포함하는지 확인
        assertTrue(toString.contains("currentPassword=CurrentPass123!"));
        assertTrue(toString.contains("newPassword=NewPass1234!"));
    }
}