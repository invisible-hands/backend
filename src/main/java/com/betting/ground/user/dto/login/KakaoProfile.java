package com.betting.ground.user.dto.login;

import lombok.Data;

@Data
public class KakaoProfile {
    private Long id;
    private KakaoAccount kakao_account;

    @Data
    public class KakaoAccount {
        private Profile profile;
        private String name;
        private String email;
        private String phone_number;

        @Data
        public class Profile {
            private String nickname;
            private String profile_image_url;
        }
    }
}
