package com.javateam.portfolio.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.javateam.portfolio.domain.BoardVO;
import com.javateam.portfolio.domain.NoticeVO;
import com.javateam.portfolio.service.BoardService;
import com.javateam.portfolio.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	NoticeService noticeService;
	
	/*
	 * @GetMapping("/") public String home() { log.info("Home!"); return "index"; }
	 */
	
	@GetMapping({"/","/index"})
	public String index(Model model) {
		log.info("index");
		
		List<BoardVO> qnas = boardService.selectBoardBySorting();
		/*
		 * qnas.forEach(x -> { log.info("qnas : " + x ); });
		 */
		model.addAttribute("qnas", qnas);

		List<NoticeVO> notices = noticeService.selectNoticeBySorting();
		/*
		 * notices.forEach(x -> { log.info("notices : " + x ); });
		 */
		model.addAttribute("notices", notices);
		
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		log.info("login");
		return "login";
	}
		
	@GetMapping("/loginError")
	public String loginError(Model model, HttpSession session) {
		
		Object secuSess = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		
		log.info("인증 오류 메시지-1 : ", secuSess);
		log.info("인증 오류 메시지-2 : ", secuSess.toString());
		
		model.addAttribute("error", "true");
		model.addAttribute("msg", secuSess);
				
		return "login";
	}
	
	@GetMapping("/403")
    public String acessDenided(Model model, HttpSession session) {
    	
		model.addAttribute("errMsg", "페이지 접근 권한이 없습니다.");
		model.addAttribute("movePage", "/index");
	
		return "/error/error";
	}	
	
    @GetMapping("/login?logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "index";
    }

}
