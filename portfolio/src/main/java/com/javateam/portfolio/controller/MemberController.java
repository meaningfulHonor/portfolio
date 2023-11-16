package com.javateam.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.portfolio.domain.CustomUser;
import com.javateam.portfolio.domain.MemberDTO;
import com.javateam.portfolio.domain.MemberUpdateDTO;
import com.javateam.portfolio.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/join.do")
	public String join(Model model) {
		
		log.info("join.do");
		
		model.addAttribute("memberDTO", new MemberDTO());
		return "/member/join";
	}
	
	@PostMapping("/joinProc.do")
	public String joinProc(@ModelAttribute("memberDTO") MemberDTO memberDTO,
							RedirectAttributes ra) {
		
		log.info("joinProc.do : {}", memberDTO);
		
		memberDTO.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
		boolean result = memberService.insertMemberRole(memberDTO);
		
		String msg = result == true ? "회원 정보 저장에 성공하였습니다." 
				: "회원정보 저장에 실패하였습니다.";
		
		log.info("result : {}", msg);
		
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/member/join.do"; // 임시조치
	}
	
	@GetMapping("/view.do")
	public String view(Model model) {
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();
										
		CustomUser customUser = (CustomUser)principal;
		
		log.info("principal : {}", principal);
		log.info("id : {}", customUser.getUsername());
		
		String id = customUser.getUsername();
		
		MemberDTO memberDTO = memberService.selectMember(id);
		
		if(memberDTO == null) {
			model.addAttribute("errMsg", "회원정보가 존재하지 않습니다.");
			return "/error/error";
		} else {
			model.addAttribute("memberDTO", memberDTO);
		}
		return "/member/view";
	}
	
	@GetMapping("/update.do")
	public String update(Model model) {
		
		// Spring Security Pricipal(Session) 조회
		Object principal = SecurityContextHolder.getContext()
											.getAuthentication()
											.getPrincipal();
		
		CustomUser customUser = (CustomUser)principal;
		log.info("principal : {}", principal);
		log.info("id : {}", customUser.getUsername()); // 로그인 아이디
		
		String id = customUser.getUsername();
		MemberDTO memberDTO = memberService.selectMember(id);
		
		if (memberDTO == null) {
			// 에러 처리
			model.addAttribute("errMsg", "회원 정보가 존재하지 않습니다.");
			return "/error/error";
			
		} else {
			
			// 주의) ClassCastException 발생 가능성 있음
			// MemberUpdateDTO memberUpdateDTO = (MemberUpdateDTO)memberDTO;
			MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO(memberDTO);
			// model.addAttribute("memberDTO", memberDTO);
			model.addAttribute("memberUpdateDTO", memberUpdateDTO);
		}
		
		return "/member/update";	
	}
	
	@PostMapping("updateProc.do")
	public String updateProc(@ModelAttribute("memberUpdateDTO") MemberUpdateDTO memberUpdateDTO,
							 RedirectAttributes ra) {
		log.info("updateProc.do : {}", memberUpdateDTO);
		
		if(memberUpdateDTO.getPassword1().trim().equals("") != true) {
			memberUpdateDTO.setPassword(bCryptPasswordEncoder.encode(memberUpdateDTO.getPassword1()));			
		}
		log.info("updateProc.do-1(암호화 이후) : {}", memberUpdateDTO);
		
		boolean result = memberService.updateMember(memberUpdateDTO);
		
		String msg = result == true ? "회원 정보 수정에 성공하였습니다."
					: "회원 정보 수정에 실패하였습니다.";
		
		log.info("result : {}", msg);
		
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/member/update.do";
		
	}
	
}
