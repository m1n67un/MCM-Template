package com.mg.core.dto.naver;

import lombok.Data;

@Data
public class NaverOauthDTO {

    private String id; // 동일인 식별
    private String nickname; // 사용자 별명
    private String name; // 사용자 이름
    private String email; // 사용자 이메일
    private String gender; // 사용자 성별
    private String age; // 사용자 나이
    private String birthday; // 사용자 생일(mm-dd)
    private String profile_image; // 사용자 프로필사진 URL
    private String birthyear; // 출생연도
    private String mobile; // 휴대전화번호

}
