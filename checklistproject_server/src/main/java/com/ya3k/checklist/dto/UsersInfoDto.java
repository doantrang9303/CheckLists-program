package com.ya3k.checklist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersInfoDto {
    private int id;
    private String fullName;
    private String email;
    private int userId;

}
