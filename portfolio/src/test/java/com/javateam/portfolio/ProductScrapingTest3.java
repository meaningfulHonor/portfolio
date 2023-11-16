package com.javateam.portfolio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.portfolio.domain.ReplicaVO;
import com.javateam.portfolio.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProductScrapingTest3 {
	
	@Autowired
	ProductService productService;
	
	private ReplicaVO productScrapOne(String productNum) throws IOException {
		
		ReplicaVO replicaVO = new ReplicaVO();
		
		int prdNum = Integer.parseInt(productNum);
		
		String productSite = "https://www.oksoccer.co.kr/product/detail.html?product_no="+productNum;
		Document doc = Jsoup.connect(productSite).get();
	
		log.info("상품코드 : "+ doc.select("div#prdInfo h3").text().substring(1,9));
		log.info("제품명 : " + doc.select("div#prdInfo h3").text().substring("[P0000UQE] ".length()));
		String price = doc.select("div#prdInfo div.xans-element- table tr td span strong#span_product_price_text").text().replace(",", "").split("원")[0];
		log.info("가격 : "+ Integer.parseInt(price));
		log.info("제조사 : " + doc.select("div#prdInfo div.xans-element- table tr td span[style=font-size:12px;color:#555555;]").text().split(" ")[0]);		
		
		// <meta property="og:image" content="https://image.yes24.com/goods/90593485/XL" />
		// 이미지 주소 : http://image.yes24.com/goods/90593485/XL.jpeg
		
		String imgPageURL = "https://www.oksoccer.co.kr/product/detail.html?product_no="+productNum;
		
		Document docImg = Jsoup.connect(imgPageURL).ignoreContentType(true).get();
		String imgURL = docImg.select("meta[property='og:image']").attr("content");
		
		log.info("imgURL : {}", imgURL);
		
		String targetImagePath = "D:/Ssong/downloads/product_images/thumbnails/replica/";
		String saveImgFileName = productNum + "_XL.jpg";
		
		log.info("saveImgFileName : {}", saveImgFileName);
		log.info("targetImagePath + saveImgFileName : " + targetImagePath + saveImgFileName);
		
		InputStream in = new URL(imgURL).openStream();
		Files.copy(in, Paths.get(targetImagePath + saveImgFileName), StandardCopyOption.REPLACE_EXISTING);
		
		replicaVO.setPrdCode(doc.select("div#prdInfo h3").text().substring(1,9));
		replicaVO.setPrdNum(prdNum);
		replicaVO.setPrdName(doc.select("div#prdInfo h3").text().substring("[P0000UQE] ".length()));
		replicaVO.setPrdPrice(Integer.parseInt(price));
		replicaVO.setPrdImg(saveImgFileName);
		replicaVO.setImgPath(targetImagePath + saveImgFileName);
		replicaVO.setCompany(doc.select("div#prdInfo div.xans-element- table tr td span[style=font-size:12px;color:#555555;]").text().split(" ")[0]);
		
		log.info("replicaVO : " + replicaVO);
		
		return replicaVO;
	}
	
	@Test
	public void productScrap() throws IOException {
				
		log.info("//////////");
		String productSite = "https://www.oksoccer.co.kr/product/list.html?cate_no=12&page=3";
		Document doc = Jsoup.connect(productSite).get();
		
		log.info("카테고리 유니폼 수 : " + doc.select("ul li.xans-record- span.zoom").size());
		log.info("카테고리 첫째 유니폼 번호 : " + doc.select("ul li.xans-record- input").attr("class").split(" ")[1].substring("xECPCNO_".length()));
		
		log.info("-------------------------------");
		
		int len = doc.select("ul li.xans-record- span.zoom").size();
		log.info("길이 : {}",len);
		for (int i=0; i<len; i+=1) {

			String productNum = doc.select("ul li.xans-record- input").get(i).attr("class").split(" ")[1].substring("xECPCNO_".length());
			log.info("카테고리 {} 유니폼 번호 : {}", i, productNum);
			ReplicaVO replicaVO = this.productScrapOne(productNum);
			log.info("---------------------------------");
			
			// 상품 저장
			productService.insertReplica(replicaVO);
		} //
		
	} //

}