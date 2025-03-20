package com.movie.plex.users;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/users/*")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailSend mailSend;
	
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
	
	@RequestMapping(value = "mailCheck", method = RequestMethod.GET)
	@ResponseBody //�޼ҵ尡 ��ȯ�� ��ü�� �ڵ����� json.xml���·� ��ȯ���ִ� ����, �����͸� ���� Ŭ���̾�Ʈ�� �����Ҥ��� ���
	public String mailCheck(String email) throws Exception {
		return mailSend.joinEmail(email);
	}
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String getLogin() throws Exception {
		return "users/login";
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String getLogin(UserDTO userDTO, HttpSession session, Model model) throws Exception {
		userDTO = userService.getLogin(userDTO);
		
		if(userDTO != null) {
			session.setAttribute("user", userDTO);
			return "redirect:/";
		}
		
		model.addAttribute("result", "�α��ν���");
		model.addAttribute("path", "./login");
		
		
		return "commons/result";
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value = "join",method = RequestMethod.GET)
	public String join() throws Exception {
		return "users/join";
	}
	
	@RequestMapping(value = "join", method = RequestMethod.POST)
	public String join(UserDTO userDTO, Model model) throws Exception {
		
		int result = userService.join(userDTO);
		
		if(result > 0) {
			model.addAttribute("result", "ȸ�����Լ���");
			model.addAttribute("path", "../");
		}
		
		return "commons/result";
	}
}
