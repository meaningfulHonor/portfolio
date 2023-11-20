package com.javateam.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.portfolio.domain.BoardDTO;
import com.javateam.portfolio.domain.MemberDTO;
import com.javateam.portfolio.domain.NoticeDTO;
import com.javateam.portfolio.domain.OrderPageItemDTO;
import com.javateam.portfolio.domain.ProductVO;
import com.javateam.portfolio.service.MemberService;
import com.javateam.portfolio.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {

	@Autowired
	ProductService productService;
	
	@Autowired 
	MemberService memberService;
	
	@GetMapping("/itemPage.do")
	public String itemPage() {
		log.info("itemPage");
		
		return "/product/itemPage";
	}
		
	@GetMapping("/itemPage.do/{prdNum}")
	public String prdNumItemPage(@PathVariable("prdNum") int prdNum, Model model) {
		// @PathVariable("id") String id,
		
		log.info("itemPage/" + prdNum + "페이지 입니다.");
		
		ProductVO productVO = productService.selectProduct(prdNum);
		log.info("productVO : ",productVO);
		model.addAttribute("product", productVO);
		
		
		/*
		 * MemberDTO memberDTO = memberService.selectMember(id);
		 * log.info("memberDTO : ",memberDTO); model.addAttribute("member", memberDTO);
		 */
		OrderPageItemDTO orderPageItemDTO= new OrderPageItemDTO(); 
		model.addAttribute("orderPageItemDTO", orderPageItemDTO);
		log.info("orderDTO : " +  new OrderPageItemDTO());
		
		return "/product/itemPage";
	}

}
