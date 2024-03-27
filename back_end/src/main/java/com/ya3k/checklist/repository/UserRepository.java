package com.ya3k.checklist.repository;

import com.ya3k.checklist.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT u FROM Users u WHERE u.user_name = :userName")
    Users findByUser(String userName);

}

