package com.javateam.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.portfolio.domain.ReplicaVO;
import com.javateam.portfolio.service.ReplicaService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/product")
@Slf4j
public class ReplicaController {

	@Autowired
	ReplicaService replicaService;
	
	@GetMapping("/itemPage2.do/{prdNum}")
	public String prdNumItemPage(@PathVariable("prdNum") int prdNum, Model model) {
		
		log.info("itemPage/" + prdNum + "페이지 입니다.");
		ReplicaVO replicaVO = replicaService.selectReplica(prdNum);
		log.info("replicaVO : ", replicaVO);
		model.addAttribute("replica", replicaVO);
		
		return "/product/itemPage2";
	}
}
