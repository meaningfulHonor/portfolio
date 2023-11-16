package com.javateam.portfolio.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.javateam.portfolio.domain.ProductVO;

public interface ProductImpl extends PagingAndSortingRepository<ProductVO, Integer> {
	
	Page<ProductVO> findAll(Pageable pageable);
	
	ProductVO findByPrdNum(int prdNum);
	
	int countByPrdNameLike(String prdName);
	int countByPrdNameContaining(String prdName);
	int countByCompanyContaining(String company);
	int countByPrdCodeContaining(String prdCode);
	
	Page<ProductVO> findByPrdNameLike(String prdName, Pageable pageable);
	Page<ProductVO> findByPrdNameContaining(String prdName, Pageable pageable);
	Page<ProductVO> findByCompanyContaining(String company, Pageable pageable);
	Page<ProductVO> findByPrdCodeContaining(String prdCode, Pageable pageable);
	
	long countByPrdNum(int prdNum);

	
}
