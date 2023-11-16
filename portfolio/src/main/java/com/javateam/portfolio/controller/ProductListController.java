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
import com.javateam.portfolio.domain.ProductVO;
import com.javateam.portfolio.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("product")
@Slf4j
public class ProductListController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("soccer")
	public String list(@RequestParam(value="currPage", defaultValue="1") int currPage,
					   @RequestParam(value="limit", defaultValue="15") int limit,
					   Model model) {
		
		log.info("상품 목록");
		List<ProductVO> productList = new ArrayList<>();
		
		int listCount = productService.selectProductCountByPrdNum();
		
		productList = productService.selectProductByPaging(currPage, limit);
		
		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, limit);
		// 현재 페이지에 보여줄 시작 페이지 수
		int startPage = 1;
		// 현재 페이지에 보여줄 마지막 페이지 수
   	    int endPage = maxPage;
		
	    PageVO pageVO = new PageVO();
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrPage(currPage);
		pageVO.setStartPage(startPage);
		
		pageVO.setPrePage(pageVO.getCurrPage()-1 < 1 ? 1 : pageVO.getCurrPage()-1);
		pageVO.setNextPage(pageVO.getCurrPage()+1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrPage()+1);
	
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("productList", productList);
		
		return "/product/soccer";	
   	 }
}
