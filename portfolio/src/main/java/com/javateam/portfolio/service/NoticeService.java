package com.javateam.portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.javateam.portfolio.dao.NoticeDAO;
import com.javateam.portfolio.domain.NoticeVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoticeService {

	@Autowired
	NoticeDAO noticeDAO;
	
	@Transactional(rollbackFor = Exception.class)
	public NoticeVO insertNotice(NoticeVO noticeVO) {
		
		return noticeDAO.save(noticeVO);
	}
	
	@Transactional(readOnly = true)
	public int selectNoticesCount() {
		
		return (int)noticeDAO.count();
	} //

	@Transactional(readOnly = true)
	public List<NoticeVO> selectNoticesByPaging(int currPage, int limit) {
				
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "noticeNum"));
		return noticeDAO.findAll(pageable).getContent();
	} //

	@Transactional(readOnly = true)
	public NoticeVO selectNotice(int noticeNum) {
		
		return noticeDAO.findById(noticeNum);
	}

	@Transactional(readOnly = true)
	public int selectNoticesCountBySearching(String searchKey, String searchWord) {

		// return searchKey.equals("notice_subject") ? noticeDAO.countByNoticeSubjectLike("%"+searchWord+"%") : 
		return searchKey.equals("notice_subject") ? noticeDAO.countByNoticeSubjectContaining(searchWord) :
			   searchKey.equals("notice_content") ? noticeDAO.countByNoticeContentContaining(searchWord) : 
			   noticeDAO.countByNoticeWriterContaining(searchWord);	
		
	}

	@Transactional(readOnly = true)
	public List<NoticeVO> selectNoticesBySearching(int currPage, int limit, String searchKey, String searchWord) {
		
		Pageable pageable = PageRequest.of(currPage-1, limit, Sort.by(Direction.DESC, "noticeNum"));
		
		// return searchKey.equals("notice_subject") ? noticeDAO.findByNoticeSubjectLike("%"+searchWord+"%", pageable).getContent() : 
		return searchKey.equals("notice_subject") ? noticeDAO.findByNoticeSubjectContaining(searchWord, pageable).getContent() :
			   searchKey.equals("notice_content") ? noticeDAO.findByNoticeContentContaining(searchWord, pageable).getContent() : 
			   noticeDAO.findByNoticeWriterContaining(searchWord, pageable).getContent();
	}

	public List<Integer> getImageList(String str, String imgUploadPath) {

		log.info("NoticeService.getImageList");
		List<Integer> imgList = new ArrayList<>(); // upload_file_tbl 테이블의 PK(기본키)
		
		if (str.contains(imgUploadPath) == false) { // 이미지 미포함
			
			log.info("이미지가 전혀 포함되어 있지 않습니다.");
			
		} else {

			// 포함된 전체 이미지 수 : 이 한계량 만큼 검색  => 카운터에 반영
			int imgLen = StringUtils.countOccurrencesOf(str, imgUploadPath);
			
			log.info("imgLen : " + imgLen);
			
			// 이미지 검색 카운터 설정 : 이미지 검색할 횟수
			int count = 0;  
			
			int initPos = str.indexOf(imgUploadPath);
			log.info("첫 발견 위치 : " + initPos);
			
			// 추출된 문자열 : 반복문에서 사용
			String subStr = str;
			
			while (count < imgLen) {
				
				initPos = subStr.indexOf(imgUploadPath);
				
				// 이미지 파일만 추출 (첫번째)
				// "/notice/image/".length()
				initPos += imgUploadPath.length();
				log.info("이미지 파일 시작 위치 : " + initPos);
				
				// 추출된 문자열
				// ex) 41 (.../notice/image/41" : upload_file_tbl 테이블의 삽입 이미지 PK(기본키))
				subStr = subStr.substring(initPos);
				
				log.info("subStr : " + subStr);
				
				// 첫번째 " (큰 따옴표) 위치 검색하여 순수한 숫자(PK)만 추출
				int quotMarkPos = subStr.indexOf("\"");
				
				// 이미지 파일 끝 검색하여 이미지 파일명/확장자 추출
				// 이미지 끝 검색 : 검색어(" )
				int imgFileNum = Integer.parseInt(subStr.substring(0, quotMarkPos));
				
				log.info("이미지 파일 테이블 PK(기본기) : " + imgFileNum);
				
				count++; // 이미지 추출되었으므로 카운터 증가
				
				imgList.add(imgFileNum); // 리스트에 추가
				
				log.info("----------------------------------------");
			
			} //  while
		
		} // if

		return imgList;
	}

	@Transactional(rollbackFor = Exception.class)
	public NoticeVO updateNotice(NoticeVO noticeVO) {
		
		return noticeDAO.save(noticeVO);
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteReplysById(int noticeNum) {
		
		boolean result = false;
		
		try {
			noticeDAO.deleteById(noticeNum);
			result = true;
		} catch (Exception e) {
			log.error("deleteReplyById error : {}", e);
			result = false;
		}
		
		return result;
	}

	// 게시글(원글) 삭제
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteById(int noticeNum) {
		
		boolean result = false;
		
		try {
			noticeDAO.deleteById(noticeNum);
			result = true;
		} catch (Exception e) {
			log.error("deleteById error : {}", e);
			result = false;
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<NoticeVO> selectNoticeBySorting(){
		
		return ((List<NoticeVO>)noticeDAO.findAll(Sort.by(Direction.DESC, "noticeReadCount"))).subList(0, 3);
		
	}

	// 게시글 조회수 갱신
	@Transactional(rollbackFor = Exception.class)
	public boolean updateNoticeReadcountByNoticeNum(int noticeNum) {
		
		boolean result = false;
		
		try {
			noticeDAO.updateNoticeReadcountByNoticeNum(noticeNum);
			result = true;
		} catch (Exception e) {
			log.error("updateNoticeReadcountByNoticeNum error : {}", e);
			result = false;
		}
		
		return result;
	}
}
