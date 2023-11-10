package com.betting.ground.common;

import com.betting.ground.config.jwt.JwtUtils;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Slf4j
//@Hidden
public class HealthController {
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Hidden
    @GetMapping
    public String check() {
        return "ok";
    }

    @Hidden
    @GetMapping("/check")
    public String scheduler() {
        log.info("check");
        return "check";
    }

    @Hidden
    @GetMapping("/redis/set")
    public String setRedisValue(@RequestParam String key, @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "key : " + key + " value : " + value;
    }

    @Hidden
    @GetMapping("/redis/get/{key}")
    public String getRedisValue(@PathVariable String key) {
        return "value: " + stringRedisTemplate.opsForValue().get(key);
    }

    @GetMapping("/login")
    public String login(@RequestParam String email) {
        // 로그인
        String password = "sdn189382hbnk2ubsd892";
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 엑세스 토큰 생성
        return jwtUtils.generateAccessTokenFromLoginUser(loginUser);
    }

    @GetMapping("/GUEST")
    public String GUEST(@AuthenticationPrincipal LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getUser().getId()).get();
        user.updateGuest();
        userRepository.save(user);
        return user.getRole().name();
    }

    @GetMapping("/USER")
    public String USER(@AuthenticationPrincipal LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getUser().getId()).get();
        user.updateRole();
        userRepository.save(user);
        return user.getRole().name();
    }


}
