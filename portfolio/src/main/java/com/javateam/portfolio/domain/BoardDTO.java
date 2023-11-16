package com.javateam.portfolio.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardDTO {
	
	private int boardNum;
	private String boardWriter;
	private String boardPass;
	private String boardSubject;
	private String boardContent;
	private MultipartFile boardFile;
	private int boardReRef;
	private int boardReLev;
	private int boardReSeq;
	private int boardReadCount;
	private Date boardDate;
	private String textMulti = "text";
	
	// 업로드 파일(파일명을 확인할 수 있도록 파일명 인쇄)
	// : boardFile.getOriginalFilename()
	@Override
	public String toString() {
		return "BoardDTO [boardNum=" + boardNum + ", boardWriter=" + boardWriter + ", boardPass=" + boardPass
				+ ", boardSubject=" + boardSubject + ", boardContent=" + boardContent + ", boardFile=" + boardFile.getOriginalFilename()
				+ ", boardReRef=" + boardReRef + ", boardReLev=" + boardReLev + ", boardReSeq=" + boardReSeq
				+ ", boardReadCount=" + boardReadCount + ", boardDate=" + boardDate + ", textMulti=" + textMulti + "]";
	}
	
}
