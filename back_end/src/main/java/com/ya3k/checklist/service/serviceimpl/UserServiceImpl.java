package com.ya3k.checklist.service.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ya3k.checklist.dto.UsersDto;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.mapper.UsersMapper;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.service.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UsersDto saveUsers(UsersDto usersDto) {
        UsersDto findUser = findByUserName(usersDto.getUserName());
        if (findUser != null) {
            findUser.setEmail(usersDto.getEmail());
            Users user = UsersMapper.toUsers(findUser);
            userRepository.save(user);
            return UsersMapper.toUsersDto(user);
        } else{
            Users user = UsersMapper.toUsers(usersDto);
            userRepository.save(user);
            return UsersMapper.toUsersDto(user);}
    }

    @Override
    public UsersDto findByUserName(String userName) {
        Users findUser = userRepository.findByUser(userName);
        if (findUser != null) {
            return UsersMapper.toUsersDto(findUser);
        }
        return null;
    }
}
