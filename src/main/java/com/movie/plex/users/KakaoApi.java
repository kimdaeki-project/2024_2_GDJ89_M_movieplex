package com.movie.plex.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


//@Component
//@PropertySource("classpath:kakao.properties")
public class KakaoApi {
//	@Value("${kakao.api}")
	private String kakaoApi;
//	@Value("${kakao.redirect_url}")
	private String kakaoRedirectUrl;
	
	public String getKakaoApi() {
		return kakaoApi;
	}

	public void setKakaoApi(String kakaoApi) {
		this.kakaoApi = kakaoApi;
	}

	public String getKakaoRedirectUrl() {
		return kakaoRedirectUrl;
	}

	public void setKakaoRedirectUrl(String kakaoRedirectUrl) {
		this.kakaoRedirectUrl = kakaoRedirectUrl;
	}

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	public String getAccessToken(String code) {
		String accessToken = "";
	    String refreshToken = "";
	    String reqUrl = "https://kauth.kakao.com/oauth/token";

	    try{
	        URL url = new URL(reqUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        
	        //�ʼ� ��� ����
	        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	        conn.setDoOutput(true); //OutputStream���� POST �����͸� �Ѱ��ְڴٴ� �ɼ�.

	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	        StringBuilder sb = new StringBuilder();
	        
	        //�ʼ� ���� �Ķ���� ����
	        sb.append("grant_type=authorization_code");
	        sb.append("&client_id=").append(kakaoApi);
	        sb.append("&redirect_uri=").append(kakaoRedirectUrl);
	        sb.append("&code=").append(code);

	        bw.write(sb.toString());
	        bw.flush();

	        int responseCode = conn.getResponseCode();
	        log.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);

	        BufferedReader br;
	        if (responseCode >= 200 && responseCode < 300) {
	            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }

	        String line = "";
	        StringBuilder responseSb = new StringBuilder();
	        while((line = br.readLine()) != null){
	            responseSb.append(line);
	        }
	        String result = responseSb.toString();
	        log.info("responseBody = {}", result);

	        JsonParser parser = new JsonParser();
	        JsonElement element = parser.parse(result);
	        accessToken = element.getAsJsonObject().get("access_token").getAsString();
	        refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

	        br.close();
	        bw.close();
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    return accessToken;
	}
	
	public HashMap<String, Object> getUserInfo(String accessToken) {
		
		 HashMap<String, Object> userInfo = new HashMap<String, Object>();
		    String reqUrl = "https://kapi.kakao.com/v2/user/me";
		    try{
		        URL url = new URL(reqUrl);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("POST");
		        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		        int responseCode = conn.getResponseCode();
		        log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);

		        BufferedReader br;
		        if (responseCode >= 200 && responseCode <= 300) {
		            br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
		        } else {
		            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"UTF-8"));
		        }

		        String line = "";
		        StringBuilder responseSb = new StringBuilder();
		        while((line = br.readLine()) != null){
		            responseSb.append(line);
		        }
		        String result = responseSb.toString();
		        log.info("responseBody = {}", result);

		        JsonParser parser = new JsonParser();
		        JsonElement element = parser.parse(result);

		        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
		        JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

		        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
		        String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

		        userInfo.put("nickname", nickname);
		        userInfo.put("email", email);

		        br.close();

		    }catch (Exception e){
		        e.printStackTrace();
		    }
		    return userInfo;
		}
	
	public void kakaoLogout(String accessToken) {
	    String reqUrl = "https://kapi.kakao.com/v1/user/unlink";

	    try{
	        URL url = new URL(reqUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

	        int responseCode = conn.getResponseCode();
	        log.info("[KakaoApi.kakaoLogout] responseCode : {}",  responseCode);

	        BufferedReader br;
	        if (responseCode >= 200 && responseCode <= 300) {
	            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }

	        String line = "";
	        StringBuilder responseSb = new StringBuilder();
	        while((line = br.readLine()) != null){
	            responseSb.append(line);
	        }
	        String result = responseSb.toString();
	        log.info("kakao logout - responseBody = {}", result);

	    }catch (Exception e){
	        e.printStackTrace();
	    }
	}
}

