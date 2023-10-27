package com.betting.ground.common;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class HealthController {
    private final StringRedisTemplate stringRedisTemplate;

    @GetMapping
    public String check() {
        return "ok";
    }

    @GetMapping("/check")
    public String scheduler() {
        log.info("check");
        return "check";
    }

    @GetMapping("/redis/set")
    public String setRedisValue(@RequestParam String key, @RequestParam String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return "key : " + key + " value : " + value;
    }

    @GetMapping("/redis/get/{key}")
    public String getRedisValue(@PathVariable String key) {
        return "value: " + stringRedisTemplate.opsForValue().get(key);
    }
}
