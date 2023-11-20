package com.javateam.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.portfolio.domain.MemberDTO;
import com.javateam.portfolio.domain.OrderPageItemDTO;
import com.javateam.portfolio.service.MemberService;
import com.javateam.portfolio.service.OrderService;
import com.javateam.portfolio.service.ProductService;
import com.javateam.portfolio.service.ReplicaService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController { 
	
	@Autowired
	MemberService memberService;

	@Autowired
	ProductService productService;

	@Autowired
	ReplicaService replicaService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/cart/{id}")
	public String orderPageGET(@PathVariable("id") String id, OrderPageItemDTO OrderPageItemDTO, Model model){
		
		MemberDTO memberDTO = orderService.selectMember(id);

		model.addAttribute("order", memberDTO);

		log.info("회원 아이디 123: {}", memberDTO.getId());

		return "/order/cart";
	}

	
}
