package com.cantiller.oauth2_profile_app.service;

import com.cantiller.oauth2_profile_app.dto.ProfileUpdateRequest;
import com.cantiller.oauth2_profile_app.entity.AuthProvider;
import com.cantiller.oauth2_profile_app.entity.User;
import com.cantiller.oauth2_profile_app.repository.AuthProviderRepository;
import com.cantiller.oauth2_profile_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String providerUserId = null;

        // Get provider user ID (different for each provider)
        Object idAttr = oAuth2User.getAttribute("id");
        if (idAttr != null) {
            providerUserId = String.valueOf(idAttr);
        } else {
            providerUserId = oAuth2User.getAttribute("sub"); // Google uses "sub"
        }

        if (providerUserId == null) {
            throw new RuntimeException("Unable to retrieve provider user ID");
        }

        final String finalProviderUserId = providerUserId; // Make it effectively final

        // Find user by provider ID instead of email
        AuthProvider authProvider = authProviderRepository.findByProviderUserId(finalProviderUserId)
                .orElseThrow(() -> new RuntimeException("User not found for provider ID: " + finalProviderUserId));

        User user = authProvider.getUser();

        // Force load the auth providers to avoid lazy loading issues
        user.getAuthProviders().size();

        return user;
    }

    @Transactional
    public User updateProfile(OAuth2User oAuth2User, ProfileUpdateRequest request) {
        User user = getCurrentUser(oAuth2User);

        user.setDisplayName(request.getDisplayName());
        user.setBio(request.getBio());

        User updatedUser = userRepository.save(user);
        log.info("Updated profile for user: {}", user.getEmail());

        return updatedUser;
    }
}