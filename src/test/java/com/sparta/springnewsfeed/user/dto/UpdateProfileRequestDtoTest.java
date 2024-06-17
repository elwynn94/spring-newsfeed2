package com.sparta.springnewsfeed.user.dto;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateProfileRequestDtoTest {

    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    public void testUpdateProfileRequestDtoCreation() {
        // Given: Fixture Monkey를 사용하여 UpdateProfileRequestDto 데이터 생성
        UpdateProfileRequestDto updateProfileRequestDto = fixtureMonkey.giveMeOne(UpdateProfileRequestDto.class);

        // When & Then: 필드들이 null이 아닌지 확인
        assertNotNull(updateProfileRequestDto.getName());
        assertNotNull(updateProfileRequestDto.getIntroduction());
    }

    @Test
    public void testUpdateProfileRequestDtoCustomData() {
        // Given: 특정 조건을 가진 UpdateProfileRequestDto 데이터 생성
        UpdateProfileRequestDto updateProfileRequestDto = fixtureMonkey.giveMeBuilder(UpdateProfileRequestDto.class)
                .set("name", "testName")
                .set("introduction", "This is a test introduction")
                .sample();

        // When & Then: 필드 값이 예상한 대로 설정되었는지 확인
        assertEquals("testName", updateProfileRequestDto.getName());
        assertEquals("This is a test introduction", updateProfileRequestDto.getIntroduction());
    }

    @Test
    public void testUpdateProfileRequestDtoEqualsAndHashCode() {
        // Given: 동일한 필드 값을 가진 두 개의 UpdateProfileRequestDto 생성
        UpdateProfileRequestDto dto1 = fixtureMonkey.giveMeBuilder(UpdateProfileRequestDto.class)
                .set("name", "testName")
                .set("introduction", "This is a test introduction")
                .sample();
        UpdateProfileRequestDto dto2 = fixtureMonkey.giveMeBuilder(UpdateProfileRequestDto.class)
                .set("name", "testName")
                .set("introduction", "This is a test introduction")
                .sample();

        // When & Then: equals 및 hashCode 검증
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testUpdateProfileRequestDtoToString() {
        // Given: 특정 조건을 가진 UpdateProfileRequestDto 데이터 생성
        UpdateProfileRequestDto updateProfileRequestDto = fixtureMonkey.giveMeBuilder(UpdateProfileRequestDto.class)
                .set("name", "testName")
                .set("introduction", "This is a test introduction")
                .sample();

        // When: toString 호출
        String toString = updateProfileRequestDto.toString();

        // Then: toString 메소드가 올바른 문자열을 포함하는지 확인
        assertTrue(toString.contains("name=testName"));
        assertTrue(toString.contains("introduction=This is a test introduction"));
    }

}