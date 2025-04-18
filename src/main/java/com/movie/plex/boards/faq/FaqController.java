package com.movie.plex.boards.faq;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.movie.plex.boards.notice.NoticeFilesDTO;
import com.movie.plex.boards.qna.QnaFilesDTO;
import com.movie.plex.pages.Pager;
import com.movie.plex.users.UserDTO;

@Controller
@RequestMapping(value = "/faq/*")
public class FaqController {

	@Autowired
	private FaqService faqService;
	
	@ModelAttribute("kind")
	public String getKind() {
		return "faq";
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Pager pager) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		List<FaqDTO> list = faqService.getList(pager);
		
		modelAndView.addObject("list", list);
		modelAndView.setViewName("board/list");
		return modelAndView;
	}
	
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public ModelAndView getDetail(FaqDTO faqDTO, HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		
		Object obj = session.getAttribute("updateFaqHit");
		boolean check = false;
		
		if(obj!=null) {
			HashSet<Long> ar = (HashSet<Long>)obj;
			if(!ar.contains(faqDTO.getFaqNum())) {
				ar.add(faqDTO.getFaqNum());
				check = true;
			}
		}else {
			HashSet<Long> num = new HashSet<Long>();
			num.add(faqDTO.getFaqNum());
			session.setAttribute("updateFaqHit", num);
			check=true;
		}
		
		faqDTO = faqService.getDetail(faqDTO, check);
		
		modelAndView.addObject("dto", faqDTO);
		modelAndView.setViewName("board/detail");
		
		return modelAndView;
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(HttpSession session, Model model) throws Exception {
		UserDTO userDTO = (UserDTO)session.getAttribute("user");
		if(userDTO==null||userDTO.getUserGrade()!= 4) {
			model.addAttribute("result", "관리자만 추가가능합니다.");
			model.addAttribute("path", "./list");
			return "commons/result";
		}else {
			model.addAttribute("user", userDTO);
			return "board/boardform";			
		}
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String add(FaqDTO faqDTO, HttpSession session, MultipartFile [] attaches) throws Exception {
		int result = faqService.add(faqDTO,session,attaches);
		String a="";
		
		if(result>0) {
			a = "redirect:./list";
		}
		
		return a;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public ModelAndView update(FaqDTO faqDTO, HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		faqDTO = faqService.getDetail(faqDTO, false);
		UserDTO userDTO = (UserDTO)session.getAttribute("user");
		
		if(userDTO==null||userDTO.getUserGrade()!= 4) {
			modelAndView.addObject("result", "관리자만 수정가능합니다.");
			modelAndView.addObject("path", "./detail?faqNum="+faqDTO.getFaqNum());
			modelAndView.setViewName("commons/result");
		}else {		
			modelAndView.addObject("dto", faqDTO);
			modelAndView.setViewName("board/boardform");
		}
		
		return modelAndView;
	}

	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView update2(FaqDTO faqDTO,HttpSession session, MultipartFile [] attaches) throws Exception {
		int result = faqService.update(faqDTO,session,attaches);
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(result > 0) {
			modelAndView.setViewName("redirect:./detail?faqNum="+faqDTO.getFaqNum());
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public ModelAndView delete(FaqDTO faqDTO, HttpSession session) throws Exception {
		UserDTO userDTO = (UserDTO)session.getAttribute("user");

		ModelAndView modelAndView = new ModelAndView();
		
		if(userDTO==null||userDTO.getUserGrade()!= 4) {
			modelAndView.addObject("result", "관리자만 삭제가능합니다.");
			modelAndView.addObject("path", "./detail?faqNum="+faqDTO.getFaqNum());
			modelAndView.setViewName("commons/result");
		} else {
			
			int result = faqService.delete(faqDTO,session);
			
			if(result > 0) {
				modelAndView.addObject("result","삭제성공");
				modelAndView.addObject("path", "./list");
			} else {
				modelAndView.addObject("result","삭제실패");
				modelAndView.addObject("path", "./detail");
			}
			modelAndView.setViewName("commons/result");
		}
		
		
		return modelAndView;
		
	}
	
	@RequestMapping(value = "fileDelete", method = RequestMethod.POST)
	public String fileDelete(FaqFilesDTO faqFilesDTO, HttpSession session, Model model) throws Exception {
		int result = faqService.fileDelete(faqFilesDTO, session);
		model.addAttribute("result", result);
		return "commons/ajax";
	}
	
	@RequestMapping(value = "fileDown", method = RequestMethod.GET)
	public String fileDown(FaqFilesDTO faqFilesDTO, Model model) throws Exception {
		faqFilesDTO = faqService.getFileDetail(faqFilesDTO);
		model.addAttribute("file", faqFilesDTO);
		return "fileDown";
	}
	
	@RequestMapping(value = "detailFiles", method = RequestMethod.POST)
	public String detailFiles (MultipartFile uploadFile,HttpSession session, Model model)throws Exception{
		String fileName = faqService.detailFiles(session, uploadFile);
		
		fileName = "/resources/images/faq/"+fileName;
		
		model.addAttribute("result", fileName);
		
		return "commons/ajax";
	}
	
	@RequestMapping(value = "detailFilesDelete", method = RequestMethod.POST)
	public String detailFilesDelete(HttpSession session, FaqFilesDTO faqFilesDTO, Model model) throws Exception {
		System.out.println("detailFilesDelete");
		faqService.deleteFile(faqFilesDTO, session);
		model.addAttribute("result", 1);
		return "commons/ajax";
	}
}
