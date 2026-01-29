package com.pelizzaris.springsecurity.config;

import com.pelizzaris.springsecurity.entities.Role;
import com.pelizzaris.springsecurity.entities.User;
import com.pelizzaris.springsecurity.repository.RoleRepository;
import com.pelizzaris.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByName("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("\nUsuário admin já existe.\n");
                },
                () -> {
                    var user = new User();
                    user.setName("admin");
                    user.setEmail("admin.pelizzaris@pelizzaris.com");
                    user.setPassword(bCryptPasswordEncoder.encode("admin123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                    System.out.println("\nUsuário admin criado com sucesso.\n");
                }
        );
    }
}
