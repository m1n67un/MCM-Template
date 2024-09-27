package com.mg.api.login.controller;

import com.mg.core.dto.google.GoogleOauthDTO;
import com.mg.core.dto.google.GoogleRequestDTO;
import com.mg.core.dto.google.GoogleResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class GoogleLoginController {

        @Value("${google.callBack-uri}")
        private String callBackUri;
        @Value("${google.client-id}")
        private String clientId;
        @Value("${google.client-secret}")
        private String clientSecret;
        @Value("${google.state}")
        private String googleState;

        @GetMapping("/google/login")
        public String google_login(HttpServletRequest request) {

                String login_url = "https://accounts.google.com/o/oauth2/v2/auth?&response_type=code"
                                + "&client_id=" + clientId
                                + "&redirect_uri=" + callBackUri
                                + "&scope=email profile";

                request.getSession().setAttribute("state", googleState);

                return login_url;

        }

        /**
         * Access Token 받기
         * 
         * @param request
         * @return
         */
        @GetMapping("/google/callback")
        @ResponseBody
        public String login(HttpServletRequest request) {
                String code = request.getParameter("code");
                RestTemplate restTemplate = new RestTemplate();
                GoogleRequestDTO param = GoogleRequestDTO
                                .builder()
                                .clientId(clientId)
                                .clientSecret(clientSecret)
                                .code(code)
                                .redirectUri(callBackUri)
                                .grantType("authorization_code").build();

                ResponseEntity<GoogleResponseDTO> resultEntity = restTemplate.postForEntity(
                                "https://oauth2.googleapis.com/token",
                                param, GoogleResponseDTO.class);

                String jwtToken = resultEntity.getBody().getId_token();
                String data = "Bearer " + jwtToken;

                return data;
        }

        /*
         * JSON 형태로 회원정보 얻기
         * 
         * @GetMapping("/google/callback")
         * public GoogleOauthDTO login(HttpServletRequest request) {
         * String code = request.getParameter("code");
         * RestTemplate restTemplate = new RestTemplate();
         * GoogleRequestDTO param = GoogleRequestDTO
         * .builder()
         * .clientId(clientId)
         * .clientSecret(clientSecret)
         * .code(code)
         * .redirectUri(callBackUri)
         * .grantType("authorization_code").build();
         * 
         * ResponseEntity<GoogleResponseDTO> resultEntity =
         * restTemplate.postForEntity("https://oauth2.googleapis.com/token",
         * param, GoogleResponseDTO.class);
         * 
         * String jwtToken = resultEntity.getBody().getId_token();
         * Map<String, String> map = new HashMap<>();
         * map.put("id_token", jwtToken);
         * GoogleOauthDTO data = new GoogleOauthDTO();
         * return data = getUserInfo(map);
         * }
         * 
         * public GoogleOauthDTO getUserInfo(Map<String, String> map) {
         * RestTemplate restTemplate = new RestTemplate();
         * ResponseEntity <GoogleOauthDTO> resultEntity =
         * restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
         * map, GoogleOauthDTO.class);
         * 
         * GoogleOauthDTO info = new GoogleOauthDTO();
         * 
         * info.setIss(resultEntity.getBody().getIss());
         * info.setAzp(resultEntity.getBody().getAzp());
         * info.setAud(resultEntity.getBody().getAud());
         * info.setSub(resultEntity.getBody().getSub());
         * info.setEmail(resultEntity.getBody().getEmail());
         * info.setEmail_verified(resultEntity.getBody().getEmail_verified());
         * info.setAt_hash(resultEntity.getBody().getAt_hash());
         * info.setName(resultEntity.getBody().getName());
         * info.setPicture(resultEntity.getBody().getPicture());
         * info.setGiven_name(resultEntity.getBody().getGiven_name());
         * info.setFamily_name(resultEntity.getBody().getFamily_name());
         * info.setLocale(resultEntity.getBody().getLocale());
         * info.setIat(resultEntity.getBody().getIat());
         * info.setExp(resultEntity.getBody().getExp());
         * info.setAlg(resultEntity.getBody().getAlg());
         * info.setKid(resultEntity.getBody().getKid());
         * info.setTyp(resultEntity.getBody().getTyp());
         * 
         * return info;
         * }
         */
}
