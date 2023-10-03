package com.veysel.repository;

import com.veysel.repository.entity.User;
import com.veysel.util.MyFactoryRepository;

public class UserRepository extends MyFactoryRepository<User,Long> {

    public UserRepository() {
        super(new User());
    }

}
