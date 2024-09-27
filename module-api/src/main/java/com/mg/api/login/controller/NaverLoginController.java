package com.mg.api.login.controller;

import com.mg.core.dto.naver.NaverOauthDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class NaverLoginController {

    @Value("${naver.callBack-uri}")
    private String callBackUri;
    @Value("${naver.client-id}")
    private String clientId;
    @Value("${naver.client-secret}")
    private String clientSecret;
    @Value("${naver.state}")
    private String naverState;

    @GetMapping("/naver/login")
    public String naver_login(HttpServletRequest request) {

        String login_url = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + callBackUri
                + "&state=" + naverState;

        request.getSession().setAttribute("state", naverState);

        return login_url;

    }

    @ResponseBody
    @GetMapping("/naver/callback")
    public String login(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://nid.naver.com/oauth2.0/token?client_id={clientId}&client_secret={clientSecret}&grant_type=authorization_code&state={state}&code={code}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        JSONObject jsonResponse = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class,
                    clientId, clientSecret, state, code);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                if (responseBody != null) {
                    jsonResponse = new JSONObject(responseBody);
                    System.out.println("Response received: " + jsonResponse.toString());
                } else {
                    System.out.println("No response body received.");
                }
            } else {
                System.out.println("HTTP error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = (String) jsonResponse.get("access_token");
        String data = "Bearer " + token;

        return data;
    }

    /*
     * JSON 으로 회원정보 얻기
     * 
     * @GetMapping("/naver/callback")
     * public NaverOauthDTO login(HttpServletRequest request) {
     * String code = request.getParameter("code");
     * String state = request.getParameter("state");
     * 
     * RestTemplate restTemplate = new RestTemplate();
     * String url =
     * "https://nid.naver.com/oauth2.0/token?client_id={clientId}&client_secret={clientSecret}&grant_type=authorization_code&state={state}&code={code}";
     * 
     * HttpHeaders headers = new HttpHeaders();
     * headers.set("Content-Type", "application/json");
     * 
     * HttpEntity<String> entity = new HttpEntity<>(headers);
     * JSONObject jsonResponse = null;
     * try {
     * ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
     * entity, String.class, clientId, clientSecret, state, code);
     * if (response.getStatusCode().is2xxSuccessful()) {
     * String responseBody = response.getBody();
     * if (responseBody != null) {
     * jsonResponse = new JSONObject(responseBody);
     * System.out.println("Response received: " + jsonResponse.toString());
     * } else {
     * System.out.println("No response body received.");
     * }
     * } else {
     * System.out.println("HTTP error: " + response.getStatusCode());
     * }
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * return getUserInfo((String) jsonResponse.get("access_token"));
     * }
     * 
     * public NaverOauthDTO getUserInfo(String accessToken) {
     * 
     * RestTemplate restTemplate = new RestTemplate();
     * String url = "https://openapi.naver.com/v1/nid/me";
     * 
     * HttpHeaders headers = new HttpHeaders();
     * headers.set("Authorization", "Bearer " + accessToken);
     * headers.set("Content-Type", "application/json");
     * 
     * HttpEntity<String> entity = new HttpEntity<>(headers);
     * ResponseEntity<String> response = restTemplate.exchange(
     * url,
     * HttpMethod.GET,
     * entity,
     * String.class
     * );
     * 
     * JSONObject responseBody = new JSONObject(response.getBody());
     * JSONObject res = responseBody.getJSONObject("response");
     * 
     * // Extract data
     * NaverOauthDTO dto = new NaverOauthDTO();
     * dto.setId(res.optString("id"));
     * dto.setNickname(res.optString("nickname"));
     * dto.setName(res.optString("name"));
     * dto.setEmail(res.optString("email"));
     * dto.setGender(res.optString("gender"));
     * dto.setAge(res.optString("age"));
     * dto.setBirthday(res.optString("birthday"));
     * dto.setProfile_image(res.optString("profile_image"));
     * dto.setBirthyear(res.optString("birthyear"));
     * dto.setMobile(res.optString("mobile"));
     * 
     * return dto;
     * 
     * }
     */

}
