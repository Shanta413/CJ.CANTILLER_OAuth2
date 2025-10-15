package com.cantiller.oauth2_profile_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "AUTHPROVIDER",
        uniqueConstraints = @UniqueConstraint(columnNames = {"\"provider\"", "\"providerUserId\""})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"userId\"", nullable = false)  // 👈 matches User.id with case-sensitive column name
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"provider\"", nullable = false)
    private Provider provider;

    @Column(name = "\"providerUserId\"", nullable = false)
    private String providerUserId;

    @Column(name = "\"providerEmail\"", nullable = false)
    private String providerEmail;

    public enum Provider {
        GOOGLE,
        GITHUB
    }
}
