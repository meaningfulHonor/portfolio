package com.javateam.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.javateam.portfolio.dao.MemberDAO;
import com.javateam.portfolio.dao.OrderDAO;
import com.javateam.portfolio.domain.MemberDTO;
import com.javateam.portfolio.domain.OrderPageItemDTO;
import com.javateam.portfolio.domain.ProductVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
	
	@Autowired
	MemberDAO memberDAO;
	
	@Autowired
	OrderDAO orderDAO;
	
	@Autowired
	TransactionTemplate txTemplate;
	
	@Transactional(readOnly = true)
	public MemberDTO selectMember(String id) {
		log.info("로그인 id : {} ", memberDAO.selectMember(id));
		return memberDAO.selectMember(id);
	}
	
	/**
	 * 개별 상품 등록
	 * 
	 * @param productVO 상품 객체
	 * @return 등록 성공 여부
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean insertCart(final OrderPageItemDTO orderPageItemDTO) {
		
		log.info("OrderService.insertCart");
		
		return txTemplate.execute(status -> {
				
				boolean flag = false;
				
				try {
						orderDAO.insertCart(orderPageItemDTO);
						log.info("상품 정보 저장 성공");
						flag = true;
					
				} catch (Exception e) {	
					flag = false;
					status.setRollbackOnly();
					log.error("상품 정보 저장 실패");
					log.error("OrderService.insertCart error : " + e.getMessage());
				} //
				
				return flag;
		});
		
	} //
	
}
