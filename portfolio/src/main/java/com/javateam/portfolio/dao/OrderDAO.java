package com.javateam.portfolio.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.portfolio.domain.OrderPageItemDTO;

@Repository
public class OrderDAO {
	
	private static final Logger log
		= LoggerFactory.getLogger(OrderPageItemDTO.class);

	@Autowired
	SqlSession sqlSession;

	private static final String MAPPER_NS 
		= "mapper.Order.";

	/**
	 * 개별 상품 등록
	 * 
	 * @param OrderPageItemDTO 상품 객체
	 */
	public void insertCart(OrderPageItemDTO orderPageItemDTO) {
		
		log.info("OrderPageItemDTO.insertCart");
		sqlSession.insert(MAPPER_NS + "insertCart", orderPageItemDTO);
	}
}
