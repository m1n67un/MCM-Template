package com.mg.core.dto.kakao;

import lombok.Data;

@Data
public class KakaoOauthLoginDTO {

    private String id;
    private String connected_at;
    private String profile_nickname_needs_agreement;
    private String profile_image_needs_agreement;
    private String nickname;
    private String thumbnail_image_url;
    private String profile_image_url;
    private String is_default_image;
    private String is_default_nickname;

    private String profile_image;
    private String thumbnail_image;

}
