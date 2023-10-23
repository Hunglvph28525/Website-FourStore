package com.poly.datn.service.impl;

import com.poly.datn.entity.User;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.request.RegisterUser;
import com.poly.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
    @Override
    public void add(User user) {
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAvatar("/assets/images/users/avatar-2.jpg");
        userRepository.save(user);
    }

    @Override
    public Optional<User> detail(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUser(String username) {
        return userRepository.getByUser(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUser(username);
    }
    @Override
    public User convert(RegisterUser source) {
        return User.builder()
                .email(source.getEmail())
                .username(source.getUserName())
                .password(source.getPassword())
                .build();
    }
}
