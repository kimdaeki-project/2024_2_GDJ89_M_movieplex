package com.movie.plex.users;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/login/oauth2/code/*")
public class KakaoController {
	
	@Autowired
	 private KakaoApi kakaoApi;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "kakao", method = RequestMethod.GET)
	public String kakaoLogin(@RequestParam String code, Model model, HttpSession session) throws Exception{
		System.out.println("kakaologin");
	    // 1. �ΰ� �ڵ� �ޱ� (@RequestParam String code)

	    // 2. ��ū �ޱ�
	    String accessToken = kakaoApi.getAccessToken(code);

	    // 3. ����� ���� �ޱ�
	    Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

	    String email = (String)userInfo.get("email");
	    String nickname = (String)userInfo.get("nickname");

	    System.out.println("email = " + email);
	    System.out.println("nickname = " + nickname);
	    System.out.println("accessToken = " + accessToken);
	    
	    
	    
	    UserDTO userDTO = userService.findEmail(email);
	    
	    if(userDTO!=null) {
	    	userDTO = userService.getLogin(userDTO);
	    	 if (userDTO.getUserOut() == 1) {
		            // ����ڰ� ��Ȱ��ȭ�� ������ ��� �α��� ���� ó��
		            model.addAttribute("result", "��Ȱ��ȭ�� ������Դϴ�. �����ڿ��� �����ϼ���.");
		            model.addAttribute("path", "/users/login");
		            return "commons/result";  // ��Ȱ��ȭ�� ����� �޽��� ���
		        }else {
		        	session.setAttribute("accessToken", accessToken);
		        	session.setAttribute("userEmail", email);
		        	session.setAttribute("userName", nickname);
		        	
		        	session.setAttribute("user", userDTO);
		        	
		        	return "redirect:/";		        	
		        }
	    	
	    } else {
	    	model.addAttribute("name", nickname);
	    	model.addAttribute("email", email);
	    	return "users/kakaoJoin";	    	
	    }

	}
	
	@RequestMapping(value = "kakao",method = RequestMethod.POST)
	public String kakaoLogin(UserDTO userDTO, HttpSession session, Model model) throws Exception {
		int result = userService.kakaoJoin(userDTO);
		
		model.addAttribute("result", "ȸ�����Լ���");
		model.addAttribute("path", "/");
		
		return "commons/result";
	}
	
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public String idCheck(UserDTO userDTO, Model model) throws Exception {
		
		userDTO = userService.idCheck(userDTO);
		//�ߺ� 0
		int result =0;
		
		if(userDTO== null) {
			result =1; //�ߺ� x
		}
		
		model.addAttribute("result", result);
		
		return "commons/ajax";
	}
	
	@RequestMapping(value = "kakao2", method = RequestMethod.GET)
	public String reviewNestkakaoLogin(@RequestParam String code, Model model, HttpSession session) throws Exception{
		System.out.println("kakaologin2");
	    // 1. �ΰ� �ڵ� �ޱ�

	    // 2. ��ū �ޱ�
	    String accessToken = kakaoApi.getAccessToken2(code);

	    // 3. ����� ���� �ޱ�
	    Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

	    String email = (String)userInfo.get("email");
	    String nickname = (String)userInfo.get("nickname");

	    System.out.println("email = " + email);
	    System.out.println("nickname = " + nickname);
	    System.out.println("accessToken = " + accessToken);
	    
	    
	    
	    UserDTO userDTO = userService.findEmail(email);
	    
	    if(userDTO!=null) {
	    	userDTO = userService.getLogin(userDTO);
	    	 if (userDTO.getUserOut() == 1) {
		            // ����ڰ� ��Ȱ��ȭ�� ������ ��� �α��� ���� ó��
		            model.addAttribute("result", "��Ȱ��ȭ�� ������Դϴ�. �����ڿ��� �����ϼ���.");
		            model.addAttribute("path", "/reviewNest/login");
		            return "commons/result";  // ��Ȱ��ȭ�� ����� �޽��� ���
		        }else {
		        	session.setAttribute("accessToken", accessToken);
		        	session.setAttribute("userEmail", email);
		        	session.setAttribute("userName", nickname);
		        	
		        	session.setAttribute("user", userDTO);
		        	
		        	return "redirect:/reviewNest";		        	
		        }
	    	
	    } else {
	    	model.addAttribute("result", "�α��ν���");
	    	model.addAttribute("path", "/reviewNest/login");
	    	return "commons/result";	    	
	    }

	}
}
