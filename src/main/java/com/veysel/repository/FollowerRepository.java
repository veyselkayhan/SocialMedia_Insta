package com.veysel.repository;

import com.veysel.repository.entity.Follower;
import com.veysel.util.MyFactoryRepository;

public class FollowerRepository extends MyFactoryRepository<Follower,Long> {

    public FollowerRepository() {
        super(new Follower());
    }

}
