package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.messaging.MessageProducer;
import com.example.demo.model.UserVO;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ObjectTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageProducer messageProducer;

    public Optional<UserVO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ObjectTransformer::modelFromEntity);
    }

    public List<UserVO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(ObjectTransformer::modelFromEntity)
                .toList();
    }

    public void saveUser(UserVO userVO) {

        User userEntity = ObjectTransformer.entityFromModel(userVO);

        // 1. saving to DB
        userRepository.save(userEntity);

        // 2. sending notification
        messageProducer.sendSignupMessage(userVO);
    }
}