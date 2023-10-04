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
@Table(name = "tbl_following")
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    long id;
    long userid;
    long followingUserId;
    @Enumerated
    FollowState followState;
    @Embedded
    BaseEntity baseEntity;

}
