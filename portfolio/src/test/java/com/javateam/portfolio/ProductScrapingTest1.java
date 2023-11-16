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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProductScrapingTest1 {
	
	@Test
	public void bookScrap() throws IOException {
		
		String bookNum = "13863";
		String bookSite = "https://www.oksoccer.co.kr/product/detail.html?product_no="+bookNum;
		Document doc = Jsoup.connect(bookSite).get();
		
		log.info("신발명 : " + doc.select("div#prdInfo h3").text());
		log.info("제조사 : " + doc.select("div#prdInfo h3").text().substring(11,15));
		/*log.info("도서명(부제2) : " + doc.select("div.gd_titArea h3.gd_nameE span.gd_feature").text());*/
		
		log.info("상품코드 : "+ doc.select("div#prdInfo h3").text().substring(1,9));
		log.info("가격 : "+ doc.select("div#prdInfo xans-element- table td strong#span_product_price_text").text());
		
		// <meta property="og:image" content="https://image.yes24.com/goods/90593485/XL" />
		// 이미지 주소 : http://image.yes24.com/goods/90593485/XL.jpeg
		
		String imgPageURL = "https://www.oksoccer.co.kr/product/detail.html?product_no="+ bookNum;
		
		Document docImg = Jsoup.connect(imgPageURL).ignoreContentType(true).get();
		String imgURL = docImg.select("meta[property='og:image']").attr("content");
		
		log.info("imgURL : {}", imgURL);
		
		String targetImagePath = "D:/Ssong/downloads/product_images/";
		String saveImgFileName = bookNum + "_XL.jpeg";
		
		log.info("saveImgFileName : {}", saveImgFileName);
		log.info("targetImagePath + saveImgFileName : " + targetImagePath + saveImgFileName);
		
		InputStream in = new URL(imgURL).openStream();
		Files.copy(in, Paths.get(targetImagePath + saveImgFileName), StandardCopyOption.REPLACE_EXISTING);
		
	}

}