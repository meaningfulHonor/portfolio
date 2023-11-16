package com.javateam.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.portfolio.domain.ProductVO;
import com.javateam.portfolio.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {

	@Autowired
	ProductService productService;
	
	@GetMapping("/itemPage.do")
	public String itemPage() {
		log.info("itemPage");
		
		return "/product/itemPage";
	}
		
	@GetMapping("/itemPage.do/{prdNum}")
	public String prdNumItemPage(@PathVariable("prdNum") int prdNum, Model model) {
		
		log.info("itemPage/" + prdNum + "페이지 입니다.");
		ProductVO productVO = productService.selectProduct(prdNum);
		log.info("productVO : ", productVO);
		model.addAttribute("product", productVO);
		
		return "/product/itemPage";
	}

}
