package com.javateam.portfolio.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.portfolio.domain.ProductVO;
import com.javateam.portfolio.domain.ReplicaVO;


@Repository
public class ProductDAO {
	
	private static final Logger log
		= LoggerFactory.getLogger(ProductDAO.class);
	
	@Autowired
	SqlSession sqlSession;
	
	private static final String MAPPER_NS 
		= "mapper.Product.";
	
	/**
	 * 개별 상품 등록
	 * 
	 * @param productVO 상품 객체
	 */
	public void insertProduct(ProductVO productVO) {
		
		log.info("ProductDAO.insertProduct");
		sqlSession.insert(MAPPER_NS + "insertProduct", productVO);
	}
		
	/**
	 * 개별 상품 등록
	 * 
	 * @param replicaVO 상품 객체
	 */
	public void insertReplica(ReplicaVO replicaVO) {
		
		log.info("ReplicaDAO.insertReplica");
		sqlSession.insert(MAPPER_NS + "insertReplica", replicaVO);
	}
}
