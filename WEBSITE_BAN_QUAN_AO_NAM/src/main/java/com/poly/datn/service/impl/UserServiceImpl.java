package com.poly.datn.service.impl;

import com.poly.datn.entity.User;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
    @Override
    public void add(User user) {
        userRepository.save(user);

    }

    @Override
    public Optional<User> detail(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUser(username);
    }
}
