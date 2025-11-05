package com.pimp.common.domain.user.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_uid")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @Column(name = "user_id", nullable = false, unique = true, length = 100)
    private String userId;

    @Column(name = "user_email", nullable = false, length = 200)
    private String email;

    @Column(name = "interlock_email", length = 200)
    private String interlockEmail;
}