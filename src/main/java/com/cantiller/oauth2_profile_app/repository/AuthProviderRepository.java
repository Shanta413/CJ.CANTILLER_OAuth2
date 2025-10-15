package com.cantiller.oauth2_profile_app.repository;

import com.cantiller.oauth2_profile_app.entity.AuthProvider;
import com.cantiller.oauth2_profile_app.entity.AuthProvider.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

    Optional<AuthProvider> findByProviderAndProviderUserId(Provider provider, String providerUserId);

    // Use JOIN FETCH to eagerly load the User
    @Query("SELECT ap FROM AuthProvider ap JOIN FETCH ap.user WHERE ap.providerUserId = :providerUserId")
    Optional<AuthProvider> findByProviderUserId(@Param("providerUserId") String providerUserId);
}