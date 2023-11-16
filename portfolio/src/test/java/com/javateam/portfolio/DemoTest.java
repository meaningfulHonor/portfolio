package com.javateam.portfolio;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.portfolio.domain.BoardVO;
import com.javateam.portfolio.domain.ProductVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class DemoTest {
	
	//private static final Logger log = LoggerFactory.getLogger(DemoTest.class);
	
	@Test
	public void test() {
		
		ProductVO productVO = new ProductVO();
		BoardVO boardVO = new BoardVO();
		log.info("#######################");
		// log.info("productVO : " + productVO);
		log.info("BoardVO : " + boardVO);
		
	}
	
	
}
