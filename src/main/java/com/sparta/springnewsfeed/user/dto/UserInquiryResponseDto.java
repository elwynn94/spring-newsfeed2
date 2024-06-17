package com.sparta.springnewsfeed.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;


public record UserInquiryResponseDto(String userId, String name, String introduction, String email, String pictureURL) {
}
