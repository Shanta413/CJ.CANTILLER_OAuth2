package com.cantiller.oauth2_profile_app.service;


import com.cantiller.oauth2_profile_app.entity.AuthProvider;
import com.cantiller.oauth2_profile_app.entity.User;
import com.cantiller.oauth2_profile_app.repository.AuthProviderRepository;
import com.cantiller.oauth2_profile_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;


    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("Processing OAuth2 login for provider: {}", registrationId);


        try {
            processOAuth2User(registrationId, oAuth2User);
        } catch (Exception e) {
            log.error("Error processing OAuth2 user for provider {}: {}", registrationId, e.getMessage(), e);
            OAuth2Error oauth2Error = new OAuth2Error("invalid_oauth2_user", "Failed to process OAuth2 user", null);
            throw new OAuth2AuthenticationException(oauth2Error, e.getMessage(), e);
        }


        return oAuth2User;
    }


    private void processOAuth2User(String registrationId, OAuth2User oAuth2User) {
        String email = null;
        String providerUserId = null;
        String displayName = null;
        String avatarUrl = null;


        // --- Handle provider-specific attributes ---
        if ("google".equalsIgnoreCase(registrationId)) {
            email = oAuth2User.getAttribute("email");
            providerUserId = oAuth2User.getAttribute("sub");
            displayName = oAuth2User.getAttribute("name");
            avatarUrl = oAuth2User.getAttribute("picture");


        } else if ("github".equalsIgnoreCase(registrationId)) {
            Object idAttr = oAuth2User.getAttribute("id");
            providerUserId = (idAttr != null) ? String.valueOf(idAttr) : null;
            email = oAuth2User.getAttribute("email"); // may be null if private
            displayName = oAuth2User.getAttribute("name");
            avatarUrl = oAuth2User.getAttribute("avatar_url");


            // fallback values
            if (displayName == null) {
                displayName = oAuth2User.getAttribute("login");
            }
            if (email == null) {
                email = displayName + "@users.noreply.github.com";
            }
        }


        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider: " + registrationId);
        }


        log.info("OAuth2 user email: {}, provider ID: {}", email, providerUserId);


        AuthProvider.Provider provider = AuthProvider.Provider.valueOf(registrationId.toUpperCase());


        // --- Check if provider link already exists ---
        var existingAuthProvider = authProviderRepository.findByProviderAndProviderUserId(provider, providerUserId);
        if (existingAuthProvider.isPresent()) {
            log.info("Existing user found for provider account");
            return;
        }


        // --- Create or find User ---
        final String userEmail = email;
        final String userDisplayName = displayName;
        final String userAvatarUrl = avatarUrl;


        User user = userRepository.findByEmail(userEmail)
                .orElseGet(() -> {
                    log.info("Creating new user for email: {}", userEmail);
                    User newUser = User.builder()
                            .email(userEmail)
                            .displayName(userDisplayName)
                            .avatarUrl(userAvatarUrl)
                            .build();
                    return userRepository.save(newUser);
                });


        // --- Link Auth Provider ---
        AuthProvider authProvider = AuthProvider.builder()
                .user(user)
                .provider(provider)
                .providerUserId(providerUserId)
                .providerEmail(userEmail)
                .build();


        authProviderRepository.save(authProvider);
        log.info("Linked {} provider to user {}", provider, user.getEmail());
    }
}

