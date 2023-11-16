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

import com.javateam.portfolio.domain.BoardDTO;
import com.javateam.portfolio.domain.BoardVO;
import com.javateam.portfolio.service.BoardService;
import com.javateam.portfolio.service.FileUploadService;
import com.javateam.portfolio.util.FileUploadUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	FileUploadService fileUploadService;
	
	@Value("${spring.servlet.multipart.max-file-size}") 
	String uploadFileMaxSize;
		
	@GetMapping("/write.do")
	public String write(Model model) {
		
		model.addAttribute("boardDTO", new BoardDTO());
		return "/board/write";
	} //
	
	@PostMapping("/writeProc.do")
	public String writeProc(@ModelAttribute("boardDTO") BoardDTO boardDTO, Model model) {
		
		log.info("BoardDTO : {}", boardDTO);
		
		BoardVO boardVO = new BoardVO(boardDTO);
		
		// 첨부 파일이 있다면...
		if (boardDTO.getBoardFile().getOriginalFilename().trim().equals("") == false) {
					
			log.info("게시글 작성 처리(첨부 파일) : {}", boardDTO.getBoardFile().getOriginalFilename());
					
			String actualUploadFilename = FileUploadUtil.encodeFilename(boardVO.getBoardFile());
			boardVO.setBoardFile(actualUploadFilename);
				
		} 
				
		log.info("BoardVO : {}", boardVO);
				
		boardVO = boardService.insertBoard(boardVO);
				
		log.info("----- 게시글 저장 BoardVO : {}", boardVO);
				
		String msg = "";
				
		if (boardDTO.getBoardFile().getOriginalFilename().trim().equals("") == false) {
					
			msg = fileUploadService.storeUploadFile(boardVO.getBoardNum(), boardDTO.getBoardFile(), boardVO.getBoardFile());
			log.info("msg : {}", msg);
		}
		
		if (boardVO != null) {
			msg = "게시글 저장에 성공하였습니다.";
		}
		
		model.addAttribute("errMsg", msg);
		// model.addAttribute("movePage", "/board/write.do");
		model.addAttribute("movePage", "/board/list.do"); // 10.23
		
		//return "/error/error"; 
		return "/board/write";
	} //
	
	@GetMapping("/view.do/{boardNum}")
	public String view(@PathVariable("boardNum") int boardNum, Model model) {
		
		BoardVO boardVO =boardService.selectBoard(boardNum);
		log.info("BoardVO : {}", boardVO);
		
		model.addAttribute("board", boardVO);

		// 조회할 때마다 조회수 갱신(+)
		boardService.updateBoardReadcountByBoardNum(boardNum);
		
		return "/board/view";
	}
					
}
