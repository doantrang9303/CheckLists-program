package com.ya3k.checklist.service.serviceinterface;
import com.ya3k.checklist.dto.UsersDto;

public interface UserService {
    UsersDto saveUsers(UsersDto usersDto);
    UsersDto findByUserName(String userName);
}