package com.javateam.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.portfolio.domain.NoticeDTO;
import com.javateam.portfolio.domain.NoticeVO;
import com.javateam.portfolio.service.NoticeService;
import com.javateam.portfolio.service.FileUploadService;
import com.javateam.portfolio.util.FileUploadUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("notice")
@Slf4j
public class NoticeController {
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	FileUploadService fileUploadService;
	
	@Value("${spring.servlet.multipart.max-file-size}") 
	String uploadFileMaxSize;
		
	@GetMapping("/write.do")
	public String write(Model model) {
		
		model.addAttribute("noticeDTO", new NoticeDTO());
		return "/notice/write";
	} //
	
	@PostMapping("/writeProc.do")
	public String writeProc(@ModelAttribute("noticeDTO") NoticeDTO noticeDTO, Model model) {
		
		log.info("NoticeDTO : {}", noticeDTO);
		
		NoticeVO noticeVO = new NoticeVO(noticeDTO);
		
		// 첨부 파일이 있다면...
		if (noticeDTO.getNoticeFile().getOriginalFilename().trim().equals("") == false) {
					
			log.info("게시글 작성 처리(첨부 파일) : {}", noticeDTO.getNoticeFile().getOriginalFilename());
					
			String actualUploadFilename = FileUploadUtil.encodeFilename(noticeVO.getNoticeFile());
			noticeVO.setNoticeFile(actualUploadFilename);
				
		} 
				
		log.info("NoticeVO : {}", noticeVO);
				
		noticeVO = noticeService.insertNotice(noticeVO);
				
		log.info("----- 게시글 저장 NoticeVO : {}", noticeVO);
				
		String msg = "";
				
		if (noticeDTO.getNoticeFile().getOriginalFilename().trim().equals("") == false) {
					
			msg = fileUploadService.storeUploadFile(noticeVO.getNoticeNum(), noticeDTO.getNoticeFile(), noticeVO.getNoticeFile());
			log.info("msg : {}", msg);
		}
		
		if (noticeVO != null) {
			msg = "게시글 저장에 성공하였습니다.";
		}
		
		model.addAttribute("errMsg", msg);
		// model.addAttribute("movePage", "/notice/write.do");
		model.addAttribute("movePage", "/notice/list.do"); // 10.23
		
		//return "/error/error"; 
		return "/notice/write";
	} //
	
	@GetMapping("/view.do/{noticeNum}")
	public String view(@PathVariable("noticeNum") int noticeNum, Model model) {
		
		NoticeVO noticeVO =noticeService.selectNotice(noticeNum);
		log.info("NoticeVO : {}", noticeVO);
		
		model.addAttribute("notice", noticeVO);

		// 조회할 때마다 조회수 갱신(+)
		noticeService.updateNoticeReadcountByNoticeNum(noticeNum);
		
		return "/notice/view";
	}
					
}
