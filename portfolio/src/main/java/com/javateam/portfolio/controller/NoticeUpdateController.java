package com.javateam.portfolio.controller;

import java.util.ArrayList;
import java.sql.Date; // 10.26
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.javateam.portfolio.domain.NoticeVO;
import com.javateam.portfolio.domain.UploadFile;
import com.javateam.portfolio.service.NoticeService;
import com.javateam.portfolio.service.FileUploadService;
import com.javateam.portfolio.service.ImageService;
import com.javateam.portfolio.service.ImageStoreService;
import com.javateam.portfolio.util.FileUploadUtil;

import lombok.extern.slf4j.Slf4j;

// 10.25
@SessionAttributes("noticeUpdateDTO")
@Controller
@RequestMapping("notice")
@Slf4j
public class NoticeUpdateController {

	@Autowired
	NoticeService noticeService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	ImageService imageService;

	@Autowired
	ImageStoreService imageStoreService;

	@GetMapping("/update.do")
	public String update(@RequestParam("noticeNum") int noticeNum, Model model, HttpSession session) {

		NoticeVO noticeVO = noticeService.selectNotice(noticeNum);
		// NoticeDTO noticeDTO = new NoticeDTO(noticeVO);
		// model.addAttribute("noticeDTO", noticeDTO);

		log.info("noticeVO(update) : {}", noticeVO);

		// 기존 정보 세션 생성
		// if (session.getAttribute("noticeUpdateSess") == null) {
		session.setAttribute("noticeUpdateSess", noticeVO);
		// }

		log.info("NoticeVO : {}", noticeVO);
		model.addAttribute("notice", noticeVO);
		// model.addAttribute("noticeUpdateDTO", new NoticeDTO());

		return "/notice/update";
	} //

	@PostMapping("/updateProc.do")
	public String updateProc(// @ModelAttribute("noticeUpdateDTO") NoticeDTO noticeUpdateDTO,
			@RequestParam Map<String, Object> map, @RequestParam("noticeFile") MultipartFile noticeFile, Model model,
			HttpSession session) {

		// log.info("------ updateProc.do : {}", noticeUpdateDTO);
		log.info("------ updateProc.do");

		log.info("첨부 파일 : {}", noticeFile.isEmpty());
		log.info("첨부 파일명 : {}", noticeFile != null ? noticeFile.getOriginalFilename() : "");

		// 수정에 실패했을 때는  글수정 화면으로 이동하고, 성공하였을 때는 개별 게시글 보기로 이동하도록 변경 
		// 성공/실패에 따라 선택적으로 화면 이동하기 위해 변수 활용(movePage)
		// 초기 기본값 변경
		// String returnPath = "redirect:/notice/view.do/" + map.get("noticeNum").toString(); // 리턴(이동) 페이지
		
		String returnPath; // 글수정 "성공/실패" 모두 "/error/error"로 가도록 재설정
		String movePage = "/notice/update.do?noticeNum=" + map.get("noticeNum").toString(); // 리턴(이동) 페이지
		
		String msg = ""; // 메시지

		map.entrySet().forEach(x -> {
			log.info(x + "");
		});

		NoticeVO defaultNoticeVO = (NoticeVO) session.getAttribute("noticeUpdateSess");
		NoticeVO updateNoticeVO = new NoticeVO(map);

		log.info("noticeUpdateSession(기존 정보) : {}", defaultNoticeVO);
		log.info("수정 정보 : {}", updateNoticeVO);

		// 게시글 패쓰워드 검증
		if (defaultNoticeVO.getNoticePass().equals(updateNoticeVO.getNoticePass().trim())) {

			// 첨부 파일 처리
			if (noticeFile.isEmpty() == false) { // 첨부 파일이 있다면

				// 저장용 파일명 암호화
				String actualUploadFilename = FileUploadUtil.encodeFilename(noticeFile.getOriginalFilename());
				updateNoticeVO.setNoticeOriginalFile(noticeFile.getOriginalFilename());
				updateNoticeVO.setNoticeFile(actualUploadFilename);

				// 신규 업로드 파일 저장(업로드)
				msg = fileUploadService.storeUploadFile(updateNoticeVO.getNoticeNum(), noticeFile,
						updateNoticeVO.getNoticeFile());
				log.info("msg : {}", msg);

				// 기존 첨부 파일 삭제
				msg += fileUploadService.deleteUploadFile(defaultNoticeVO.getNoticeFile());

				log.info("msg : {}", msg);

			} else { // 첨부 파일이 없다면...

				log.info("첨부 파일이 없다면...");

				// 기존 파일을 입력
				String originalFilename = defaultNoticeVO.getNoticeOriginalFile() != null
						? defaultNoticeVO.getNoticeOriginalFile()
						: null;
				updateNoticeVO.setNoticeOriginalFile(originalFilename);

				String encNoticeFilename = defaultNoticeVO.getNoticeFile() != null ? defaultNoticeVO.getNoticeFile() : null;
				updateNoticeVO.setNoticeFile(encNoticeFilename);

			} //

			// 글내용(noticeContent) 비교 : 변경시에는 기존 삽입 이미지 삭제 등 처리
			log.info("기존 글내용 : {}", defaultNoticeVO.getNoticeContent());
			log.info("수정 글내용 : {}", updateNoticeVO.getNoticeContent());

			/*
			 * *****************************************************************************
			 * *******************
			 */

			// 글내용이 실제로 변경되었다면... (서로 내용이 다른 경우)
			if (defaultNoticeVO.getNoticeContent().trim().equals(updateNoticeVO.getNoticeContent().trim()) == false) {

				// 기존 데이터의 삽입 이미지 목록(삽입 이미지 테이블(upload_file_tbl)의 PK(기본키)) 확보
				//
				// ex) 글내용중 이미지가 들어간 내용 ex) <img src="/portfolio2/notice/image/18" .....

				List<Integer> defaultImgList = noticeService.getImageList(defaultNoticeVO.getNoticeContent().trim(),
						"/notice/image/");
				List<Integer> updateImgList = noticeService.getImageList(updateNoticeVO.getNoticeContent().trim(),
						"/notice/image/");

				log.info("----------------------------------");

				for (int s : defaultImgList) {
					log.info("--- 기존 업로드 이미지 : " + s);
				} //

				for (int s : updateImgList) {
					log.info("--- 신규 업로드 이미지 : " + s);
				} //

				log.info("----------------------------------");

				// 삭제할 글내용에 삽입 이미지 목록
				List<Integer> deleteExpectedImgList = new ArrayList<>();

				/* ----------------------------------------------------------------------- */
				/* ----------------------------------------------------------------------- */

				// 기존에 이미지가 되어 있지만
				// 신규에는 이미지가 없을 때는 기존 이미지 모두 삭제
				// TODO
				
				if (updateImgList.size() == 0) {
					
					log.info("기존 글내용의 모든 이미지 삭제");
					deleteExpectedImgList.addAll(defaultImgList);
					
				} else { // 신규에 이미지 포함시 선택 삭제
					
					log.info("기존글의 이미지들의 선별적 삭제");
					
					if  (defaultImgList.size() > 0) {
					
						for (int s : defaultImgList) {
							
							if (updateImgList.contains(s) == false) { //
								
								log.info("실제 삭제할 기존 이미지 기본키(PK) : " + s);
								deleteExpectedImgList.add(s);
							} //
							
						} // for
					
					} // if 
					
					// 삭제할 이미지들 출력
					deleteExpectedImgList.forEach(x -> {
						log.info("삭제할 이미지 아이디 : {}", x);
					});
					
					log.info("--------------- 삭제할 이미지들 실제 삭제 -------------------");

					// 대상 삽입 이미지 파일 삭제 : 삭제할 이미지 있으면 삭제
				
					if (deleteExpectedImgList.size() > 0) {
					
						for (int imageId : deleteExpectedImgList) {
							
							// 삽입 이미지 테이블(upload_file_tbl)에서 저장경로/파일명 가져옴(file_path 필드)
							// D:/Ssong/works/portfolio2/upload/image/2023/10/20/a92e1a28f7e746b39afe7e83eb97a5d2.jpg
							UploadFile uploadFile = imageService.load(imageId); // 삭제할 이미지 파일 경로 확보 :
																				// uploadFile.getFilePath()
							// 삽입 이미지 삭제 삭제
							log.info("삭제 메시지 : {}", fileUploadService.deleteImageFile(uploadFile.getFilePath()));
							// 삽입 이미지 테이블(upload_file_tbl)에서도 해당 이미지 수록 내용 삭제
							imageStoreService.deleteById(imageId);
							
							log.info("이미지를 삭제하였습니다.");
						} // for
						
					} // if (deleteExpectedImgList.size() > 0) {
					

				} // if (updateImgList.size() == 0) {

				/* ----------------------------------------------------------------------- */
				/* ----------------------------------------------------------------------- */

			} // 글내용이 실제로 변경되었다면... (서로 내용이 다른 경우)

			else { // 변경 내용이 없다면...

				msg = "게시글 수정(변경) 내용이 없습니다.";

			} // if
				// (defaultNoticeVO.getNoticeContent().trim().equals(updateNoticeVO.getNoticeContent())
				// == false) {

			/*
			 * *****************************************************************************
			 * *******************
			 */

			// 등록일 => 최근 수정일로 변경
			updateNoticeVO.setNoticeDate(new Date(System.currentTimeMillis()));

			log.info("최종 게시글 수정 내용 : {}", updateNoticeVO);

			// 게시글 수정			
			NoticeVO resultVO = noticeService.updateNotice(updateNoticeVO);

			if (resultVO == null) {

				msg = "게시글 수정에 실패하였습니다.";

			} else {

				log.info("최종 저장 결과 : " + resultVO);
				msg = "게시글 수정에 성공하였습니다.";

				// 수정에 실패했을 때는  글수정 화면으로 이동하고, 성공하였을 때는 개별 게시글 보기로 이동하도록 변경 
				// 게시글 수정 성공후 개별 게시글 보기로 이동
				movePage = "/notice/view.do/" + map.get("noticeNum").toString();
			} //

		} else { // 패쓰워드 틀렸을 때

			msg = "게시글 패쓰워드가 틀렸습니다. 다시 입력하십시오.";

		} //

		model.addAttribute("errMsg", msg);

		// 수정에 실패했을 때는  글수정 화면으로 이동하고, 성공하였을 때는 개별 게시글 보기로 이동하도록 변경 
		// 초기값은 메서드 초기에 언급된 지역 변수에서 변경(movePage)
		// model.addAttribute("movePage", "/notice/update.do?noticeNum=" + map.get("noticeNum").toString());
		model.addAttribute("movePage", movePage);
		returnPath = "/error/error"; // 에러 페이지로 이동

		return returnPath;
	}

} //