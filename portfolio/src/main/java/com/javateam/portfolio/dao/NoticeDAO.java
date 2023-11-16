package com.javateam.portfolio.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.javateam.portfolio.domain.NoticeVO;

public interface NoticeDAO extends PagingAndSortingRepository<NoticeVO, Integer> {

	NoticeVO save(NoticeVO noticeVO);
	
	long count();
	
	Page<NoticeVO> findAll(Pageable pageable);
	
	NoticeVO findById(int noticeNum);
	
	int countByNoticeSubjectLike(String noticeSubject);
	int countByNoticeSubjectContaining(String noticeSubject);
	int countByNoticeContentContaining(String noticeContent);
	int countByNoticeWriterContaining(String noticeWriter);
	
	Page<NoticeVO> findByNoticeSubjectLike(String noticeSubject, Pageable pageable);
	Page<NoticeVO> findByNoticeSubjectContaining(String noticeSubject, Pageable pageable);
	Page<NoticeVO> findByNoticeContentContaining(String noticeContent, Pageable pageable);
	Page<NoticeVO> findByNoticeWriterContaining(String noticeWriter, Pageable pageable);
	 
	// 게시글 조회수 갱신
	@Modifying
	@Query(value = "UPDATE notice_tbl SET " + 
				   "notice_readcount = notice_readcount + 1 " + 
				   "WHERE notice_num = :noticeNum", nativeQuery = true)
	void updateNoticeReadcountByNoticeNum(@Param("noticeNum") int noticeNum);

	
}
