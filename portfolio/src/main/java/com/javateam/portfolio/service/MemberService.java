package com.javateam.portfolio.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.portfolio.dao.MemberDAO;
import com.javateam.portfolio.domain.MemberDTO;
import com.javateam.portfolio.domain.Role;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {
	
	@Autowired
	MemberDAO memberDAO;
	
	@Transactional
	public boolean insertMemberRole(MemberDTO memberDTO) {
		
		boolean result = false;
		
		// 예외처리
//		memberDAO.insertMember(memberDTO);
//		memberDAO.insertRole(new Role(memberDTO.getId(), "ROLE_USER"));
		
		try {
			memberDAO.insertMember(memberDTO);
			result = true;
		} catch(Exception e) {
			log.error("MemberService.insertMemberRole.insertMember : {}", e);
			e.printStackTrace();
		}
		
		result = false;
		
		try {
			memberDAO.insertRole(new Role(memberDTO.getId(), "ROLE_USER"));
			result = true;
		} catch (Exception e) {
			log.error("MemberService.insertMemberRole.insertRole : {}", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public boolean hasFld(String fld, String val) {
		
		boolean result = false;
		
		result = memberDAO.hasFld(fld,val);
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public MemberDTO selectMember(String id) {
		return memberDAO.selectMember(id);
	}
	
	@Transactional(readOnly = true)
	public boolean hasFldForUpdate(String id, String fld, String val) {
		return memberDAO.hasFldForUpdate(id, fld, val);
	}
	
	@Transactional
	public boolean updateMember(MemberDTO memberDTO) {
		
		boolean result = false;
		
		try {
		memberDAO.updateMember(memberDTO);
		result = true;
		} catch (Exception e) {
			log.error("MemberService.updateMember : {}", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public int selectMembersCount() {
		return memberDAO.selectMembersCount();
	}
	
	@Transactional(readOnly = true)
	public List<MemberDTO> selectMembersByPaging(int page, int limit){
		return memberDAO.selectMembersByPaging(page, limit);
	}
		
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectMembersWithRolesByPaging(int page, int limit){
		return memberDAO.selectMembersWithRolesByPaging(page, limit);
	}
	
	@Transactional
	public boolean deleteRoleById(String id, String role) {
		boolean result = false;
		
		try {
			memberDAO.deleteRoleById(id, role);
			result = true;
		} catch(Exception e) {
			log.error("MemberService.deleteRoleById : {}", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Transactional
	public boolean insertRole(Role role) {
		
		boolean result = false;
		
		try {
			memberDAO.insertRole(role);
			result = true;
		} catch (Exception e) {
			result = false;
			log.error("MemberService.insertRole : {}", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean updateRoles(String id, boolean roleUserYn, boolean roleAdminYn) {
		
		boolean result = false;
		
		List<String> roles = memberDAO.seletRolesById(id);
		
		// 회원(ROLE_USER)이면서 관리자 권한이 없는 경우
		if (roleAdminYn == true &&
			roles.contains("ROLE_USER") == true &&
			roles.contains("ROLE_ADMIN") == false) {
			log.info("관리자 권한 할당");
			
			Role role = new Role();
			role.SetUsername(id);
			role.setRole("ROLE_ADMIN");
			
			result = this.insertRole(role);
		} else if (roleAdminYn == false &&
				   roles.contains("ROLE_USER") == true &&
				   roles.contains("ROLE_ADMIN") == true) {
			log.info("관리자 권한 회수");
			
			String role = "ROLE_ADMIN";
			result = this.deleteRoleById(id, role);
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectMembersWithRolesBySearching(int page, int limit, String searchKey, String searchWord){
		return memberDAO.selectMembersWithRolesBySerching(page, limit, searchKey, searchWord);
	}
	
	@Transactional(readOnly = true)
	public int selectMembersCountBySearching(String searchKey, String searchWord) {
		return memberDAO.selectMembersCountBySearching(searchKey, searchWord);
	}
	
	@Transactional
	public boolean changeEnabled(String id, int enabled) {
		
		boolean result = false;
		
		try {
			memberDAO.changeEnabled(id, enabled);
			result = true;
		} catch (Exception e) {
			result = false;
			log.error("MemberService.changeEnabled : {}", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Transactional
	public boolean deleteMember(String id) {
		
		boolean result = false;
		
		try {
			memberDAO.deleteRolesById(id);
			result = true;
		} catch (Exception e) {
			result = false;
			log.error("MemberService.deleteMember-1 (role) : {}", e);
			e.printStackTrace();
		}
		
		result = false;
		
		try {
			memberDAO.deleteMemberById(id);
			result = true;
		} catch (Exception e) {
			result = false;
			log.error("MemberService.deleteMember-2 (member_info) : {}", e);
			e.printStackTrace();
		}
		
		return result;
	}
}
