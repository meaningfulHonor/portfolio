package com.javateam.portfolio.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
	
	// 번호
	private int cno;
	
	// 상품 코드
	private String prdCode;
	
	// 상품 번호
	private int prdNum ;
	
	// 상품 이름
	private String prdName;
	
	// 상품 가격
	private int prdPrice;
	
	// 썸네일 사진
	private MultipartFile prdImg;

	// 사진파일 원이름
	private String imgPath;
	
	// 제조사
	private String company;
	public ProductDTO() {}
	
	// VO -> DTO
	public ProductDTO(ProductVO productVO) {
		
		this.cno = productVO.getCno();
		this.prdCode = productVO.getPrdCode();
		this.prdNum = productVO.getPrdNum();
		this.prdName = productVO.getPrdName();
		this.prdPrice = productVO.getPrdPrice();
		// this.prdImg = productVO.getPrdImg();
		this.imgPath = productVO.getImgPath();
		this.company = productVO.getCompany();
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductDTO [cno=")
			.append(cno)
			.append(", prdCode=")
			.append(prdCode)
			.append(", prdNum=")
			.append(prdNum)
			.append(", prdName=")
			.append(prdName)
			.append(", prdPrice=")
			.append(prdPrice)
			.append(", prdImg=")
			.append(prdImg==null ? "" : prdImg.getOriginalFilename()) // 파일명 출력
			.append(", imgPathe=")
			.append(imgPath)
			.append(", company=")
			.append(company)
			.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cno;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((imgPath == null) ? 0 : imgPath.hashCode());
		result = prime * result + ((prdCode == null) ? 0 : prdCode.hashCode());
		result = prime * result + ((prdImg == null) ? 0 : prdImg.hashCode());
		result = prime * result + ((prdName == null) ? 0 : prdName.hashCode());
		result = prime * result + prdNum;
		result = prime * result + prdPrice;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDTO other = (ProductDTO) obj;
		if (cno != other.cno)
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (imgPath == null) {
			if (other.imgPath != null)
				return false;
		} else if (!imgPath.equals(other.imgPath))
			return false;
		if (prdCode == null) {
			if (other.prdCode != null)
				return false;
		} else if (!prdCode.equals(other.prdCode))
			return false;
		if (prdImg == null) {
			if (other.prdImg != null)
				return false;
		} else if (!prdImg.equals(other.prdImg))
			return false;
		if (prdName == null) {
			if (other.prdName != null)
				return false;
		} else if (!prdName.equals(other.prdName))
			return false;
		if (prdNum != other.prdNum)
			return false;
		if (prdPrice != other.prdPrice)
			return false;
		return true;
	}
	
}
