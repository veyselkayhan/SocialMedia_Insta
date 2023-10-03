package com.veysel;

import com.veysel.repository.UserRepository;
import com.veysel.repository.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Main main= new Main();
        UserRepository userRepository= new UserRepository();
        User user= User.builder()
                .name("Muhammet Hoca2")
                .username("muhammedHoca2")
                .build();
//        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);


    }

}