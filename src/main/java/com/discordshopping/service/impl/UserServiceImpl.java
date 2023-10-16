package com.discordshopping.service.impl;

import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;
import com.discordshopping.mapper.UserMapper;
import com.discordshopping.service.UserService;
import com.discordshopping.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> getById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional
    public User create(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);

        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public boolean create(User user) {
        if (userRepository.findById(user.getId()).isPresent()){
            return false;
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean existByPasswordAndEmail(String password, String email) {
        return userRepository.findUserByPasswordAndEmail(password, email).isPresent();
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
