package com.javateam.portfolio.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.javateam.portfolio.domain.ProductVO;
import com.javateam.portfolio.domain.ReplicaVO;

public interface ReplicaImpl extends PagingAndSortingRepository<ReplicaVO, Integer> {
	
	Page<ReplicaVO> findAll(Pageable pageable);
	
	ReplicaVO findByPrdNum(int prdNum);
	
	int countByPrdNameLike(String prdName);
	int countByPrdNameContaining(String prdName);
	int countByCompanyContaining(String company);
	int countByPrdCodeContaining(String prdCode);
	
	Page<ReplicaVO> findByPrdNameLike(String prdName, Pageable pageable);
	Page<ReplicaVO> findByPrdNameContaining(String prdName, Pageable pageable);
	Page<ReplicaVO> findByCompanyContaining(String company, Pageable pageable);
	Page<ReplicaVO> findByPrdCodeContaining(String prdCode, Pageable pageable);
	
	long countByPrdNum(int prdNum);

	
}
