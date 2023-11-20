package com.javateam.portfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.portfolio.domain.PageVO;
import com.javateam.portfolio.domain.ReplicaVO;
import com.javateam.portfolio.service.ReplicaService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("product")
@Slf4j
public class ReplicaSearchController {
	
	@Autowired
	ReplicaService replicaService;

	@GetMapping("searchReplica.do")
	public String list(@RequestParam(value="currPage", defaultValue="1") int currPage,
					   @RequestParam(value="limit", defaultValue="15") int limit,
					   @RequestParam(value="searchKey") String searchKey,
					   @RequestParam(value="searchWord") String searchWord,
					   Model model) {
		
		log.info("게시글 검색 목록");
		log.info("검색 구분 : {}", searchKey);
		log.info("검색어 : {}", searchWord);
		
		List<ReplicaVO> replicaList = new ArrayList<>();

		int replicaCount = replicaService.selectReplicasCountBySearching(searchKey, searchWord.trim());
		
		replicaList = replicaService.selectReplicasBySearching(currPage, limit, searchKey, searchWord.trim());	
		
		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(replicaCount, limit);
		// 시작 페이지 수
		int startPage = 1;
		// 마지막 페이지 수
   	    int endPage = maxPage;
   	    
   	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(replicaCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(currPage);
		pageVO.setStartPage(startPage);
		
		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
	
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("replicaList", replicaList);
		
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("searchWord", searchWord);
		
		return "/product/replica";		
	} //
}
