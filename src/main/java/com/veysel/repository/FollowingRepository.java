package com.veysel.repository;

import com.veysel.repository.entity.Following;
import com.veysel.util.MyFactoryRepository;

public class FollowingRepository extends MyFactoryRepository<Following,Long> {
    public FollowingRepository() {
        super(new Following());
    }
}
