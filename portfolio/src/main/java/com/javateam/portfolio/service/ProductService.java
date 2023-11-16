package com.javateam.portfolio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.javateam.portfolio.dao.ProductDAO;
import com.javateam.portfolio.dao.ProductImpl;
import com.javateam.portfolio.domain.ProductVO;
import com.javateam.portfolio.domain.ReplicaVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
	
	@Autowired
	ProductDAO dao;
	
	@Autowired
	ProductImpl impl;
	
	@Autowired
	TransactionTemplate txTemplate;
	
	@Autowired
	DataSourceTransactionManager txManager;
	
	/**
	 * 개별 상품 등록
	 * 
	 * @param productVO 상품 객체
	 * @return 등록 성공 여부
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean insertProduct(final ProductVO productVO) {
		
		log.info("ProductService.insertProduct");
		
		return txTemplate.execute(status -> {
				
				boolean flag = false;
				
				try {
						dao.insertProduct(productVO);
						log.info("상품 정보 저장 성공");
						flag = true;
					
				} catch (Exception e) {	
					flag = false;
					status.setRollbackOnly();
					log.error("상품 정보 저장 실패");
					log.error("ProductService.insertProduct error : " + e.getMessage());
				} //
				
				return flag;
		});
		
	} //

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean insertReplica(final ReplicaVO replicaVO) {
		
		log.info("ProductService.insertReplica");
		
		return txTemplate.execute(status -> {
			
			boolean flag = false;
			
			try {
				dao.insertReplica(replicaVO);
				log.info("상품 정보 저장 성공");
				flag = true;
				
			} catch (Exception e) {	
				flag = false;
				status.setRollbackOnly();
				log.error("상품 정보 저장 실패");
				log.error("ProductService.insertReplica error : " + e.getMessage());
			} //
			
			return flag;
		});
		
	} //
	
	@Transactional(readOnly = true)
	public List<ProductVO> selectProductByPaging(int currPage, int limit) {
				
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "prdNum"));
		return impl.findAll(pageable).getContent();
	} //
		
	@Transactional(readOnly = true)
	public ProductVO selectProduct(int prdNum) {
		
		return impl.findByPrdNum(prdNum);
	}
	
	@Transactional(readOnly = true)
	public List<ProductVO> selectProductsBySearching(int currPage, int limit, String searchKey, String searchWord) {
		
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "prdNum"));
		
		return searchKey.equals("prd_name") ? impl.findByPrdNameContaining(searchWord, pageable).getContent() :
			   searchKey.equals("company") ? impl.findByCompanyContaining(searchWord, pageable).getContent() : 
			   impl.findByPrdCodeContaining(searchWord, pageable).getContent();
	}
	
	@Transactional(readOnly = true)
	public int selectProductsCountBySearching(String searchKey, String searchWord) {

		return searchKey.equals("prdName") ? impl.countByPrdNameContaining(searchWord) :
			   searchKey.equals("company") ? impl.countByCompanyContaining(searchWord) : 
				impl.countByPrdCodeContaining(searchWord);	
		
	}
	
	@Transactional(readOnly = true)
	public int selectProductCountByPrdNum() {
		
		//return (int)impl.countByPrdNum(1); // 
		return (int)impl.count();
	} //
	
}
