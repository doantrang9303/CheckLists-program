package com.ya3k.checklist.mapper;

import com.ya3k.checklist.dto.UsersDto;
import com.ya3k.checklist.entity.Users;

public class UsersMapper {
    public static UsersDto toUsersDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setUserId(users.getUser_id());
        usersDto.setUserName(users.getUser_name());
        usersDto.setEmail(users.getEmail());
        return usersDto;
    }

    public static Users toUsers(UsersDto usersDto) {
        Users users = new Users();
        users.setUser_id(usersDto.getUserId());
        users.setUser_name(usersDto.getUserName());
        users.setEmail(usersDto.getEmail());
        return users;
    }
}