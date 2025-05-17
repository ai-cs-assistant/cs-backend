package com.olga.aics.entity;

import com.olga.aics.util.SnowflakeIdGenerator;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String role;

    private boolean enabled;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (id == null) {
            id = new SnowflakeIdGenerator().nextId();
        }
    }

}
