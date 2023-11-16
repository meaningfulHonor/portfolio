package com.javateam.portfolio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="REPLICA_TBL")
public class ReplicaVO {
	
	// 번호
	@Id
	@Column(name="CNO")
	private int cno;
	
	// 상품 코드
	@Column(name="PRD_CODE")
	private String prdCode;
	
	// 상품 번호
	@Column(name="PRD_NUM")
	private int prdNum ;
	
	// 상품 이름
	@Column(name="PRD_NAME")
	private String prdName;
	
	// 상품 가격
	@Column(name="PRD_PRICE")
	private int prdPrice;
	
	// 썸네일 사진
	@Column(name="PRD_IMG")
	private String prdImg;

	// 사진파일 원이름
	@Column(name="IMG_PATH")
	private String imgPath;
	
	// 제조사
	@Column(name="COMPANY")
	private String company;
	
	public ReplicaVO() {}
	
	// DTO => VO
	public ReplicaVO(ReplicaDTO replicaDTO) {
		
		this.cno = replicaDTO.getCno();
		this.prdCode = replicaDTO.getPrdCode();
		this.prdNum = replicaDTO.getPrdNum();
		this.prdName = replicaDTO.getPrdName();
		this.prdPrice = replicaDTO.getPrdPrice();
		this.prdImg = replicaDTO.getPrdImg().getOriginalFilename();
		this.imgPath = replicaDTO.getImgPath();
		this.company = replicaDTO.getCompany();
		
	}



	public int getCno() {
		return cno;
	}

	public void setCno(int cno) {
		this.cno = cno;
	}

	public String getPrdCode() {
		return prdCode;
	}

	public void setPrdCode(String prdCode) {
		this.prdCode = prdCode;
	}

	public int getPrdNum() {
		return prdNum;
	}

	public void setPrdNum(int prdNum) {
		this.prdNum = prdNum;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public int getPrdPrice() {
		return prdPrice;
	}

	public void setPrdPrice(int prdPrice) {
		this.prdPrice = prdPrice;
	}

	public String getPrdImg() {
		return prdImg;
	}

	public void setPrdImg(String prdImg) {
		this.prdImg = prdImg;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "ProductVO [cno=" + cno + ", prdCode=" + prdCode + ", prdNum=" + prdNum + ", prdName=" + prdName
				+ ", prdPrice=" + prdPrice + ", prdImg=" + prdImg + ", imgPath=" + imgPath + ", company=" + company
				+ "]";
	}


}
