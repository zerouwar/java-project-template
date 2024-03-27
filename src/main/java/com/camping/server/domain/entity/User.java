package com.camping.server.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体
 */
@Getter
@Setter
@Entity
@Table(
        name="user"
)
public class User extends DateAudit{
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String openId;

    @Column
    private String username;

    @Column
    private String avatar;
}
