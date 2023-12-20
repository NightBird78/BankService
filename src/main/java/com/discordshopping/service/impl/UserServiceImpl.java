package com.discordshopping.service.impl;

import com.discordshopping.exception.AlreadyExistsException;
import com.discordshopping.exception.InvalidEmailException;
import com.discordshopping.service.repository.AccountRepository;
import com.discordshopping.util.Util;
import com.discordshopping.entity.User;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.dto.UserCreatedDto;
import com.discordshopping.dto.UserDto;
import com.discordshopping.dto.UserUpdatedDto;
import com.discordshopping.entity.enums.AccountStatus;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.UserMapper;
import com.discordshopping.service.UserService;
import com.discordshopping.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountRepository accountRepository;

    @Override
    public User findById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
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
        if (userRepository.findById(user.getId()).isPresent()) {
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
    public User findByPasswordAndEmail(String password, String email) {
        return userRepository.findUserByPasswordAndEmail(password, email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public UserDto findDtoByEmail(String email) {
        return userMapper.userToDto(findByEmail(email));
    }

    @Override
    public boolean existById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDto findDtoById(String id) {
        return userMapper.userToDto(findById(id));
    }

    @Override
    public UserDto merge(UserUpdatedDto uuDto, String id) {

        User user = userMapper.merge(userMapper.dtoToUser(uuDto), findById(id));

        userRepository.save(user);

        return userMapper.userToDto(user);
    }

    @Transactional
    @Override
    public UserDto create(UserCreatedDto dto) {

        dto.setPassword(Util.encode(dto.getPassword()).toString());

        if (!Util.isValidEmail(dto.getEmail())) {
            throw new InvalidEmailException(ErrorMessage.INVALID_EMAIL_FORMAT);
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new AlreadyExistsException(ErrorMessage.DATA_EXIST);
        }

        User user = userMapper.dtoToUser(dto);
        user = userRepository.save(user);

        UserAccount uAccount = new UserAccount();
        uAccount.setUser(user);
        uAccount.setAccountStatus(AccountStatus.Active);
        uAccount.setBalance(BigDecimal.ZERO);
        uAccount.setIdba(Util.createBankIdentifier());

        accountRepository.save(uAccount);

        return userMapper.userToDto(user);
    }
}
