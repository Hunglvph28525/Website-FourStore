package com.poly.datn.service.impl;

import com.poly.datn.entity.User;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.request.forgot_passwort.UserForgotPasswordRequest;
import com.poly.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        userRepository.save(user);
    }

    @Override
    public Optional<User> detail(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getByUserName(String userName) {
        return userRepository.getByUser(userName);
    }


    /**
     *
     * @param email
     * @return
     * nếu mà ko tìm đc email thì ko tồn tại -> đăng ký
     * nếu mà email tồn tại -> lỗi
     *
     */
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.getByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUser(username).get();
    }


    @Override
    public User convert(UserSignUpRequest source) {
        return User.builder()
                .email(source.getEmail())
                .username(source.getUserName())
                .password(source.getPassword())
                .build();
    }

    @Override
    public Optional<User> changePassword(UserForgotPasswordRequest userForgotPasswordRequest) {

        Optional<User> userOptional = this.userRepository.getByUser(userForgotPasswordRequest.getUserName());

        userOptional.ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(userForgotPasswordRequest.getPassword()));
            userRepository.save(user);
            System.out.println("\n\n\t user user saved with password: " + userForgotPasswordRequest.getPassword() + "encoded: " + user.getPassword() + "\n\n\t");
        });

        return userOptional;
    }
}