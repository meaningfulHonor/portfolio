package com.javateam.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.portfolio.domain.NoticeVO;
import com.javateam.portfolio.domain.PageVO;
import com.javateam.portfolio.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("notice")
@Slf4j
public class NoticeListController {
	
	@Autowired
	NoticeService noticeService;

	@GetMapping("list.do")
	public String list(@RequestParam(value="currPage", defaultValue="1") int currPage,
					   @RequestParam(value="limit", defaultValue="10") int limit,
					   Model model) {
		
		log.info("게시글 목록");
		List<NoticeVO> noticeList = new ArrayList<>();
		
//		// 총 게시글 수
		int listCount = noticeService.selectNoticesCount();
		
		noticeList = noticeService.selectNoticesByPaging(currPage, limit);
		
		// 총 게시글 수 (댓글들을 제외한)
		// int listCount = noticeService.selectNoticesCountWithoutReplies();
		// 댓글들 제외
		// noticeList = noticeService.selectNoticesByPagingWithoutReplies(currPage, limit);
		
		// 총 페이지 수
   		// int maxPage=(int)((double)listCount/limit+0.95); //0.95를 더해서 올림 처리
		int maxPage = PageVO.getMaxPage(listCount, limit);
		// 현재 페이지에 보여줄 시작 페이지 수 (1, 11, 21,...)
   		// int startPage = (((int) ((double)currPage / 10 + 0.9)) - 1) * 10 + 1;
		int startPage = PageVO.getStartPage(currPage, limit);
		// 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30, ...)
   	    int endPage = startPage + 10;
   	    
   	    if (endPage> maxPage) endPage = maxPage;
   	    
   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(currPage);
		pageVO.setStartPage(startPage);
		
		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
	
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("noticeList", noticeList);
		
		return "/notice/list";		
	} //
	
	
}
