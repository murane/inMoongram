package com.team.auth;

import com.team.user.User;
import com.team.user.UserRepository;
import com.team.user.dto.input.SignupInput;
import com.team.user.dto.output.SignupOutput;
import com.team.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupOutput signup(SignupInput input) {
        if (userRepository.findByEmail(input.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 사용된 이메일 주소입니다.");
        }
        if (userRepository.findByNickname(input.getNickname()).orElse(null) != null) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        User saveUser = userRepository.save(
                User.builder()
                        .email(input.getEmail())
                        .nickname(input.getNickname())
                        .name(input.getName())
                        .password(passwordEncoder.encode(input.getPassword()))
                        .build()
        );

        return new SignupOutput(saveUser);
    }

    public void sendVerificationMail(String email, String nickname) {
        String VERIFICATION_LINK = "http://localhost:8080/verify/";
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), nickname, 60 * 20); // 20 minutes
        emailService.mailSend(email, "[inMoongram] 회원가입 인증메일입니다.", VERIFICATION_LINK + uuid);
    }

    public Boolean verifyEmail(String key) {
        String savedNickname = redisUtil.getData(key);
        if (userRepository.findByNickname(savedNickname).orElse(null) == null) {
            return false;
        }
        redisUtil.deleteData(key);
        return true;
    }

}
