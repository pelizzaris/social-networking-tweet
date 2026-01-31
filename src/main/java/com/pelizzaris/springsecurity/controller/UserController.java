package com.pelizzaris.springsecurity.controller;

import com.pelizzaris.springsecurity.controller.dto.UserRequestDto;
import com.pelizzaris.springsecurity.entities.Role;
import com.pelizzaris.springsecurity.entities.User;
import com.pelizzaris.springsecurity.repository.RoleRepository;
import com.pelizzaris.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping(value = "/create")
    public ResponseEntity<Void> newUser(@RequestBody UserRequestDto userRequestDto) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        if(basicRole == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Basic role not found");
        }

        var userFromDb = userRepository.findByName(userRequestDto.name());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setName(userRequestDto.name());
        user.setEmail(userRequestDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listAll(Authentication authentication) {
        authentication.getAuthorities().forEach(a -> System.out.println("Autoridade detectada: " + a.getAuthority()));

        var findAll = userRepository.findAll();
        return ResponseEntity.ok(findAll);
    }
}
