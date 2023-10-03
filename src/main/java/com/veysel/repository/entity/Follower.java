package com.veysel.repository.entity;

import com.veysel.repository.enums.FollowState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_follower")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    long id;
    long userid;
    long followerUserId;
    FollowState followState;
    @Embedded
    BaseEntity baseEntity;
}
