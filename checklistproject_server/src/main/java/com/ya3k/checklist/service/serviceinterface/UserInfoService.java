package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.UsersInfoDto;

public interface UserInfoService {
UsersInfoDto createUserInfo(UsersInfoDto usersInfoDto, String userName);
}
