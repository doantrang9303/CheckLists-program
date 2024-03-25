package com.ya3k.checklist.repository;

import com.ya3k.checklist.entity.UserInfo;
import com.ya3k.checklist.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersInfoRepository extends JpaRepository<UserInfo, Integer> {
    @Query("SELECT u FROM UserInfo u WHERE u.user = :getUsers")
    UserInfo findByUser(Users getUsers);
}
