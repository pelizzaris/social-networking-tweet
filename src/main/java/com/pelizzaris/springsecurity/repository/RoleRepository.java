package com.pelizzaris.springsecurity.repository;

import com.pelizzaris.springsecurity.entities.Role;
import com.pelizzaris.springsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
