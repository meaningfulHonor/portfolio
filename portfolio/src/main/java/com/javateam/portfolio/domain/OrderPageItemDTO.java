package com.javateam.portfolio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="order_tbl")
@Getter
@Setter
public class OrderPageItemDTO {

	@Id
	@Column(name="id")
	private String id;
	
	/* 주문 상품 명 */
	@Column(name="prd_name")
	private String prdName;
	
	/* 주문 상품 번호  */
	@Column(name="prd_num")
	private int prdNum;
	
	/* 주문 상품 수량 */
	@Column(name="prd_quantity")
	private int prdQuantity;

	/* 주문 상품 가격  */
	@Column(name="prd_price")
	private int prdPrice;
	
	/* 주문 상품 총 가격  */
	@Column(name="total_price")
	private int totalPrice;

	@Override
	public String toString() {
		return "OrderPageItemDTO [id=" + id + ", prdName=" + prdName + ", prdNum=" + prdNum + ", prdQuantity="
				+ prdQuantity + ", prdPrice=" + prdPrice + ", totalPrice=" + totalPrice + "]";
	}

	public void initSaleTotal() {
		this.totalPrice = (int)(this.prdPrice * this.prdQuantity);
	}
	
}
