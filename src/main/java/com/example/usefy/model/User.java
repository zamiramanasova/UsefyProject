package com.example.usefy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter          // ВАЖНО: именно эта аннотация даёт getUsername(), getPasswordHash()
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;       // → будет getUsername()

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;   // → будет getPasswordHash()
}
