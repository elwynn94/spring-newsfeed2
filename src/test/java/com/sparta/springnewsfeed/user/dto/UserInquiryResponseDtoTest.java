package com.sparta.springnewsfeed.user.dto;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserInquiryResponseDtoTest {

    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    public void testUserInquiryResponseDtoCreation() {
        // Given: Fixture Monkey를 사용하여 UserInquiryResponseDto 데이터 생성
        UserInquiryResponseDto userInquiryResponseDto = fixtureMonkey.giveMeOne(UserInquiryResponseDto.class);

        // When & Then: 필드들이 null이 아닌지 확인
        assertNotNull(userInquiryResponseDto.getUserId());
        assertNotNull(userInquiryResponseDto.getName());
        assertNotNull(userInquiryResponseDto.getIntroduction());
        assertNotNull(userInquiryResponseDto.getEmail());
        assertNotNull(userInquiryResponseDto.getPictureURL());
    }

    @Test
    public void testUserInquiryResponseDtoCustomData() {
        // Given: 특정 조건을 가진 UserInquiryResponseDto 데이터 생성
        UserInquiryResponseDto userInquiryResponseDto = fixtureMonkey.giveMeBuilder(UserInquiryResponseDto.class)
                .set("userId", "testUser1234")
                .set("name", "Test Name")
                .set("introduction", "This is a test introduction")
                .set("email", "testuser@example.com")
                .set("pictureURL", "http://example.com/picture.jpg")
                .sample();

        // When & Then: 필드 값이 예상한 대로 설정되었는지 확인
        assertEquals("testUser1234", userInquiryResponseDto.getUserId());
        assertEquals("Test Name", userInquiryResponseDto.getName());
        assertEquals("This is a test introduction", userInquiryResponseDto.getIntroduction());
        assertEquals("testuser@example.com", userInquiryResponseDto.getEmail());
        assertEquals("http://example.com/picture.jpg", userInquiryResponseDto.getPictureURL());
    }

    @Test
    public void testUserInquiryResponseDtoEqualsAndHashCode() {
        // Given: 동일한 필드 값을 가진 두 개의 UserInquiryResponseDto 생성
        UserInquiryResponseDto dto1 = fixtureMonkey.giveMeBuilder(UserInquiryResponseDto.class)
                .set("userId", "testUser1234")
                .set("name", "Test Name")
                .set("introduction", "This is a test introduction")
                .set("email", "testuser@example.com")
                .set("pictureURL", "http://example.com/picture.jpg")
                .sample();
        UserInquiryResponseDto dto2 = fixtureMonkey.giveMeBuilder(UserInquiryResponseDto.class)
                .set("userId", "testUser1234")
                .set("name", "Test Name")
                .set("introduction", "This is a test introduction")
                .set("email", "testuser@example.com")
                .set("pictureURL", "http://example.com/picture.jpg")
                .sample();

        // When & Then: equals 및 hashCode 검증
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testUserInquiryResponseDtoToString() {
        // Given: 특정 조건을 가진 UserInquiryResponseDto 데이터 생성
        UserInquiryResponseDto userInquiryResponseDto = fixtureMonkey.giveMeBuilder(UserInquiryResponseDto.class)
                .set("userId", "testUser1234")
                .set("name", "Test Name")
                .set("introduction", "This is a test introduction")
                .set("email", "testuser@example.com")
                .set("pictureURL", "http://example.com/picture.jpg")
                .sample();

        // When: toString 호출
        String toString = userInquiryResponseDto.toString();

        // Then: toString 메소드가 올바른 문자열을 포함하는지 확인
        assertTrue(toString.contains("userId=testUser1234"));
        assertTrue(toString.contains("name=Test Name"));
        assertTrue(toString.contains("introduction=This is a test introduction"));
        assertTrue(toString.contains("email=testuser@example.com"));
        assertTrue(toString.contains("pictureURL=http://example.com/picture.jpg"));
    }

}