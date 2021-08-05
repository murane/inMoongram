package com.team.authUtil;

import com.team.auth.CustomUserDetailsService;
import com.team.security.jwt.TokenProvider;
import com.team.user.User;
import com.team.user.UserRepository;
import com.team.user.dto.request.SignupRequest;
import io.restassured.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestAuthProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public Cookie getAccessTokenCookie(String email, String name, String nickName) {
        SignupRequest signupRequest = SignupRequest.builder()
                .email(email)
                .name(name)
                .nickname(nickName)
                .password("testpw1234~!")
                .build();
        String accessToken = getUserAccessToken(signupRequest);
        return new Cookie.Builder("accessToken", accessToken).build();
    }

    private String getUserAccessToken(SignupRequest request) {
        registerUser(request);
        Authentication authentication = getAuthentication(request.getEmail(), request.getPassword());
        return tokenProvider.createAccessToken(authentication);
    }

    private void registerUser(SignupRequest request) {
        userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );
    }

    private Authentication getAuthentication(String email, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password);

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
