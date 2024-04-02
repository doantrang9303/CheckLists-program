package com.ya3k.checklist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {
    private Integer userId;
    @JsonProperty("username")
    private String userName;
    @JsonProperty("email")
    private String email;
}