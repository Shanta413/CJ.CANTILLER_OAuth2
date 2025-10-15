package com.cantiller.oauth2_profile_app.controller;

import com.cantiller.oauth2_profile_app.dto.ProfileUpdateRequest;
import com.cantiller.oauth2_profile_app.entity.User;
import com.cantiller.oauth2_profile_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        User user = userService.getCurrentUser(oAuth2User);

        // Get the login provider from the first auth provider
        String loginProvider = user.getAuthProviders().isEmpty() ? null :
                user.getAuthProviders().get(0).getProvider().name();

        model.addAttribute("user", user);
        model.addAttribute("loginProvider", loginProvider);
        model.addAttribute("profileUpdateRequest", new ProfileUpdateRequest(
                user.getDisplayName(),
                user.getBio()
        ));

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal OAuth2User oAuth2User,
                                @Valid @ModelAttribute ProfileUpdateRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            User user = userService.getCurrentUser(oAuth2User);
            String loginProvider = user.getAuthProviders().isEmpty() ? null :
                    user.getAuthProviders().get(0).getProvider().name();
            model.addAttribute("user", user);
            model.addAttribute("loginProvider", loginProvider);
            return "profile";
        }

        try {
            userService.updateProfile(oAuth2User, request);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            log.error("Error updating profile", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile");
        }

        return "redirect:/profile";
    }
}