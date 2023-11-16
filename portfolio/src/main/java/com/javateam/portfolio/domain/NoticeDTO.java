package com.javateam.portfolio.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NoticeDTO {
	
	private int noticeNum;
	private String noticeWriter;
	private String noticePass;
	private String noticeSubject;
	private String noticeContent;
	private MultipartFile noticeFile;

	private int noticeReadCount;
	private Date noticeDate;
	private String textMulti = "text";
	
	@Override
	public String toString() {
		return "NoticeDTO [noticeNum=" + noticeNum + ", noticeWriter=" + noticeWriter + ", noticePass=" + noticePass
				+ ", noticeSubject=" + noticeSubject + ", noticeContent=" + noticeContent + ", noticeFile=" + noticeFile
				+ ", noticeReadCount=" + noticeReadCount + ", noticeDate=" + noticeDate + ", textMulti=" + textMulti
				+ "]";
	}
	
}
