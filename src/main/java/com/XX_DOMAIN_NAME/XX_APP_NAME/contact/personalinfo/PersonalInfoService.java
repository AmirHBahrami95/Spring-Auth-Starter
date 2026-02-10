package com.XX_DOMAIN_NAME.XX_APP_NAME.contact.personalinfo;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.User;
import com.XX_DOMAIN_NAME.XX_APP_NAME.userbase.user.UserService;
import com.XX_DOMAIN_NAME.XX_APP_NAME.utils.BusinessLogicException;

@Service
public class PersonalInfoService {
	
	private final Logger log = LoggerFactory.getLogger(PersonalInfoService.class);
	
	@Autowired
	private PersonalInfoRepo personalInfoRepo;
	
	@Autowired
	private UserService uServ;
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('CONTRIBUTOR')") // contributor can be a tester too!
	@Transactional
	public int delTestData() {
		return personalInfoRepo.deleteTestPis();
	}
	
	@PostAuthorize("returnObject.get().id==#pi.id or hasRole('ADMIN')")
	@Transactional
	public Optional<PersonalInfo> updateExisting(PersonalInfo pi){
		if(pi.getId()==null) {
			log.warn("probable personalInfo object injection attempt:{}",pi);
			throw new BusinessLogicException("err.personal_info.no_id", 400);
		}
		Optional<User> found=uServ.getByPersonalInfoId(pi.getId());
		if(found==null)
			throw new BusinessLogicException("err.personal_info.not_found", 404);
		if(!found.get().getPersonalInfo().update(pi))
			throw new BusinessLogicException("err.personal_info.nothing_to_change",400);
		
		personalInfoRepo.save(found.get().getPersonalInfo());
		return Optional.of(found.get().getPersonalInfo());
	}
	
}
