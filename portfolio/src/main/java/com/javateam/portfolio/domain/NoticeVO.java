package com.javateam.portfolio.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javateam.portfolio.util.FileUploadUtil;

import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name="notice_tbl")
@Slf4j
public class NoticeVO implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 게시글 번호 */
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE,
						generator = "BOARD_SEQ_GENERATOR")
		@SequenceGenerator(
						name = "BOARD_SEQ_GENERATOR",
						sequenceName = "notice_seq",
						initialValue = 1,
						allocationSize = 1)
		@Column(name = "notice_num")
		private int noticeNum;
		
		/** 게시글 작성자 */
		@Column(name = "notice_writer")
		private String noticeWriter; 
		
		/** 게시글 비밀번호 */
		@Column(name = "notice_pass")
		private String noticePass; 
		
		/** 게시글 제목 */
		@Column(name = "notice_subject")
		private String noticeSubject; 
		
		/** 게시글 내용 */
		@Column(name = "notice_content")
		private String noticeContent; 
		
		/** 첨부 파일(원래 파일명) */
		@Column(name = "notice_original_file")
		private String noticeOriginalFile; 
		
		/** 첨부 파일(인코딩된 파일명) */
		@Column(name = "notice_file")
		private String noticeFile; 
		
		/** 게시글 조회수 */
		@Column(name = "notice_readcount")
		private int noticeReadCount = 0; 
		
		/** 게시글 작성일자 */
		@Column(name = "notice_date")
		@CreationTimestamp // 작성 날짜(기본값) 생성
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") 
		private Date noticeDate;
		
		public NoticeVO() {}
		
		// NoticeDTO -> NoticeVO
	    public NoticeVO(NoticeDTO notice) {
	        
	        this.noticeNum = notice.getNoticeNum();
	        this.noticeWriter = notice.getNoticeWriter();
	        this.noticePass = notice.getNoticePass();
	        this.noticeSubject = notice.getNoticeSubject();
	        this.noticeContent = notice.getNoticeContent();
	        this.noticeOriginalFile = notice.getNoticeFile().getOriginalFilename(); // 파일명 저장
	        this.noticeFile = notice.getNoticeFile().getOriginalFilename(); // 파일명 저장
	        
	        // 첨부 파일 유무 : 없으면 => "", 있으면 => 암호화 
	        this.noticeFile = notice.getNoticeFile().getOriginalFilename().trim().equals("") ?
	        		"" : FileUploadUtil.encodeFilename(notice.getNoticeFile().getOriginalFilename());
	        		
	        this.noticeReadCount = notice.getNoticeReadCount();
	        this.noticeDate = notice.getNoticeDate();
	    }
	    
	    // 게시글 수정시 : Map<String, Object> => NoticeVO
	    public NoticeVO(Map<String, Object> map) {

	    	log.info("NoticeVO 오버로딩 생성자 : Map to VO");
	    	
	    	this.noticeNum = Integer.parseInt(map.get("noticeNum").toString());
	        this.noticeWriter = (String)map.get("noticeWriter");
	        this.noticePass = (String)map.get("noticePass");
	        this.noticeSubject = (String)map.get("noticeSubject");
	        this.noticeContent = (String)map.get("noticeContent");
	        this.noticeOriginalFile = (MultipartFile)map.get("noticeOriginal") == null ? "" : ((MultipartFile)map.get("noticeOriginal")).getOriginalFilename(); // 파일명 저장
	        // this.noticeFile = (MultipartFile)map.get("noticeFile") == null ? "" : ((MultipartFile)map.get("noticeFile")).getOriginalFilename(); // 파일명 저장
	        // this.noticeReadCount = Integer.parseInt(map.get("noticeReadCount").toString()); // 조회수 제외
	        this.noticeDate = (Date)map.get("noticeDate");
	    }
	    
		public int getNoticeNum() {
			return noticeNum;
		}

		public void setNoticeNum(int noticeNum) {
			this.noticeNum = noticeNum;
		}

		public String getNoticeWriter() {
			return noticeWriter;
		}

		public void setNoticeWriter(String noticeWriter) {
			this.noticeWriter = noticeWriter;
		}

		public String getNoticePass() {
			return noticePass;
		}

		public void setNoticePass(String noticePass) {
			this.noticePass = noticePass;
		}

		public String getNoticeSubject() {
			return noticeSubject;
		}

		public void setNoticeSubject(String noticeSubject) {
			this.noticeSubject = noticeSubject;
		}

		public String getNoticeContent() {
			return noticeContent;
		}

		public void setNoticeContent(String noticeContent) {
			this.noticeContent = noticeContent;
		}

		public String getNoticeOriginalFile() {
			return noticeOriginalFile;
		}

		public void setNoticeOriginalFile(String noticeOriginalFile) {
			this.noticeOriginalFile = noticeOriginalFile;
		}

		public String getNoticeFile() {
			return noticeFile;
		}

		public void setNoticeFile(String noticeFile) {
			this.noticeFile = noticeFile;
		}

		public int getNoticeReadCount() {
			return noticeReadCount;
		}

		public void setNoticeReadCount(int noticeReadCount) {
			this.noticeReadCount = noticeReadCount;
		}

		public Date getNoticeDate() {
			return noticeDate;
		}

		public void setNoticeDate(Date noticeDate) {
			this.noticeDate = noticeDate;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("NoticeVO [noticeNum=").append(noticeNum).append(", noticeWriter=").append(noticeWriter)
					.append(", noticePass=").append(noticePass).append(", noticeSubject=").append(noticeSubject)
					.append(", noticeContent=").append(noticeContent).append(", noticeOriginalFile=").append(noticeOriginalFile)
					.append(", noticeFile=").append(noticeFile).append(", noticeReadCount=")
					.append(noticeReadCount).append(", noticeDate=").append(noticeDate)
					.append("]");
			return builder.toString();
		}

		
		// 게시글 수정시 기존 정보와 수정 정보 동일성 여부 점검시 활용
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((noticeContent == null) ? 0 : noticeContent.hashCode());
			result = prime * result + ((noticeFile == null) ? 0 : noticeFile.hashCode());
			result = prime * result + noticeNum;
			result = prime * result + ((noticeOriginalFile == null) ? 0 : noticeOriginalFile.hashCode());
			result = prime * result + ((noticePass == null) ? 0 : noticePass.hashCode());
			result = prime * result + ((noticeSubject == null) ? 0 : noticeSubject.hashCode());
			result = prime * result + ((noticeWriter == null) ? 0 : noticeWriter.hashCode());
			return result;
		}

		
		// 게시글 수정시 기존 정보와 수정 정보 동일성 여부 점검시 활용
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof NoticeVO)) {
				return false;
			}
			NoticeVO other = (NoticeVO) obj;
			
			if (noticeContent == null) {
				if (other.noticeContent != null) {
					return false;
				}
			} else if (!noticeContent.equals(other.noticeContent)) {
				return false;
			}
			if (noticeFile == null) {
				if (other.noticeFile != null) {
					return false;
				}
			} else if (!noticeFile.equals(other.noticeFile)) {
				return false;
			}
			if (noticeNum != other.noticeNum) {
				return false;
			}
			if (noticeOriginalFile == null) {
				if (other.noticeOriginalFile != null) {
					return false;
				}
			} else if (!noticeOriginalFile.equals(other.noticeOriginalFile)) {
				return false;
			} 
			
			if (noticePass == null) {
				if (other.noticePass != null) {
					return false;
				}
			} else if (!noticePass.equals(other.noticePass)) {
				return false;
			}
			if (noticeSubject == null) {
				if (other.noticeSubject != null) {
					return false;
				}
			} else if (!noticeSubject.equals(other.noticeSubject)) {
				return false;
			}
			if (noticeWriter == null) {
				if (other.noticeWriter != null) {
					return false;
				}
			} else if (!noticeWriter.equals(other.noticeWriter)) {
				return false;
			}
			return true;
		}
	   		
}
