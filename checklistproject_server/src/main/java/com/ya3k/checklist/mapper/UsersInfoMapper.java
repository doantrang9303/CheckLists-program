package com.ya3k.checklist.mapper;

import com.ya3k.checklist.dto.UsersInfoDto;
import com.ya3k.checklist.entity.UserInfo;

public class UsersInfoMapper {
    public static UsersInfoDto mapToDto(UserInfo userInfo) {
        UsersInfoDto usersInfoDto = new UsersInfoDto();
        usersInfoDto.setId(userInfo.getId());
        usersInfoDto.setFullName(userInfo.getFullName());
        usersInfoDto.setEmail(userInfo.getEmail());
        usersInfoDto.setUserId(userInfo.getUser().getUser_id());
        return usersInfoDto;
    }
    public static UserInfo mapDtoToUserInfo(UsersInfoDto usersInfoDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(usersInfoDto.getId());
        userInfo.setFullName(usersInfoDto.getFullName());
        userInfo.setEmail(usersInfoDto.getEmail());
      return userInfo;
    }
}
