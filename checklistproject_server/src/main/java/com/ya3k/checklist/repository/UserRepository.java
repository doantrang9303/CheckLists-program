package com.ya3k.checklist.repository;

import com.ya3k.checklist.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

}

