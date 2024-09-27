package com.mg.api.login.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mg.core.dto.kakao.KakaoOauthLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class KakaoLoginController {

    private final RestTemplate restTemplate;
    @Value("${kakao.callBack-uri}")
    private String callBackUri;
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String clientSecret;
    @Value("${kakao.state}")
    private String kakaoState;

    public KakaoLoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/kakao/login")
    public String kakao_login(HttpServletRequest request) {
        String login_url = "https://kauth.kakao.com/oauth/authorize?&response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + callBackUri
                + "&state=" + kakaoState;

        return login_url;
    }

    /**
     * Access Token 값 받기
     * 
     * @param request
     * @return
     */
    @GetMapping("/kakao/callback")
    @ResponseBody
    public String login(HttpServletRequest request) {
        String code = request.getParameter("code");
        String requestURL = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret); // 필수X
        params.add("code", code);
        params.add("redirect_uri", callBackUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> entity = new HttpEntity<>(params, headers);
        String data = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<HashMap> result = restTemplate.postForEntity(requestURL, entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            System.out.println(resMap);
            String accessToken = resMap.get("access_token");
            data = "Bearer " + accessToken;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /*
     * // JSON 형태로 회원정보 받기
     * 
     * @GetMapping("/kakao/callback")
     * public KakaoOauthLoginDTO login(HttpServletRequest request) {
     * String code = request.getParameter("code");
     * String requestURL = "https://kauth.kakao.com/oauth/token";
     * 
     * MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
     * params.add("grant_type", "authorization_code");
     * params.add("client_id", clientId);
     * params.add("client_secret", clientSecret); // 필수X
     * params.add("code", code);
     * params.add("redirect_uri", callBackUri);
     * 
     * HttpHeaders headers = new HttpHeaders();
     * headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
     * 
     * HttpEntity<?> entity = new HttpEntity<>(params, headers);
     * KakaoOauthLoginDTO data = null;
     * try {
     * RestTemplate restTemplate = new RestTemplate();
     * restTemplate.getMessageConverters().add(new
     * MappingJackson2HttpMessageConverter());
     * 
     * ResponseEntity<HashMap> result = restTemplate.postForEntity(requestURL,
     * entity, HashMap.class);
     * Map<String, String > resMap = result.getBody();
     * 
     * System.out.println(resMap);
     * String accessToken = resMap.get("access_token");
     * data = getUserInfo(accessToken);
     * 
     * } catch ( Exception e ) {
     * e.printStackTrace();
     * }
     * return data;
     * }
     * 
     * 
     * public KakaoOauthLoginDTO getUserInfo(String accessToken) {
     * KakaoOauthLoginDTO data = new KakaoOauthLoginDTO();
     * Gson gson = new Gson();
     * String requestURL = "https://kapi.kakao.com/v2/user/me";
     * HttpHeaders headers = new HttpHeaders();
     * headers.set("Authorization", "Bearer " + accessToken);
     * 
     * HttpEntity<String> entity = new HttpEntity<>(headers);
     * 
     * ResponseEntity<HashMap> result = restTemplate.postForEntity(requestURL,
     * entity, HashMap.class);
     * Map<String, Object> resultMap = result.getBody();
     * System.out.println(resultMap);
     * 
     * String jsonResponse = gson.toJson(resultMap);
     * JsonObject element = JsonParser.parseString(jsonResponse).getAsJsonObject();
     * if( element.has("id") && !element.get("id").isJsonNull() ) {
     * data.setId(element.get("id").getAsString());
     * }
     * if( element.has("connected_at") && !element.get("connected_at").isJsonNull()
     * ) {
     * data.setConnected_at(element.get("connected_at").getAsString());
     * }
     * if ( element.has("kakao_account") &&
     * !element.get("kakao_account").isJsonNull() ) {
     * JsonObject kakao_account = element.getAsJsonObject("kakao_account");
     * if( kakao_account.has("profile_nickname_needs_agreement") &&
     * !kakao_account.get("profile_nickname_needs_agreement").isJsonNull()){
     * data.setProfile_nickname_needs_agreement(kakao_account.get(
     * "profile_nickname_needs_agreement").getAsString());
     * }
     * if( kakao_account.has("profile_image_needs_agreement") &&
     * !kakao_account.get("profile_image_needs_agreement").isJsonNull()) {
     * data.setProfile_image_needs_agreement(kakao_account.get(
     * "profile_image_needs_agreement").getAsString());
     * }
     * }
     * if ( element.has("kakao_account") &&
     * !element.get("kakao_account").isJsonNull() ) {
     * JsonObject kakao_account = element.getAsJsonObject("kakao_account");
     * if ( kakao_account.has("profile") &&
     * !kakao_account.get("profile").isJsonNull() ) {
     * JsonObject profile = kakao_account.getAsJsonObject("profile");
     * if( profile.has("nickname") && !profile.get("nickname").isJsonNull() ) {
     * data.setNickname(profile.get("nickname").getAsString());
     * }
     * if( profile.has("thumbnail_image_url") &&
     * !profile.get("thumbnail_image_url").isJsonNull() ) {
     * data.setThumbnail_image_url(profile.get("thumbnail_image_url").getAsString())
     * ;
     * }
     * if( profile.has("profile_image_url") &&
     * !profile.get("profile_image_url").isJsonNull() ) {
     * data.setProfile_image_url(profile.get("profile_image_url").getAsString());
     * }
     * if( profile.has("is_default_image") &&
     * !profile.get("is_default_image").isJsonNull() ) {
     * data.setIs_default_image(profile.get("is_default_image").getAsString());
     * }
     * if( profile.has("is_default_nickname") &&
     * !profile.get("is_default_nickname").isJsonNull() ) {
     * data.setIs_default_nickname(profile.get("is_default_nickname").getAsString())
     * ;
     * }
     * }
     * }
     * if ( element.has("properties") && !element.get("properties").isJsonNull() ) {
     * JsonObject properties = element.getAsJsonObject("properties");
     * if( properties.has("profile_image") &&
     * !properties.get("profile_image").isJsonNull() ) {
     * data.setProfile_image(properties.get("profile_image").getAsString());
     * }
     * if( properties.has("thumbnail_image") &&
     * !properties.get("thumbnail_image").isJsonNull() ) {
     * data.setThumbnail_image(properties.get("thumbnail_image").getAsString());
     * }
     * }
     * return data;
     * 
     * }
     */
}
