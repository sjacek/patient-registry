package com.grinnotech.patientsorig.service;

import com.grinnotech.patientsorig.dao.authorities.RequireAdminAuthority;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemService {

    private final MailService mailService;

	public SystemService(MailService mailService) {
		this.mailService = mailService;
	}

	@RequestMapping(path = "/sendtestemail", method = RequestMethod.GET)
    @RequireAdminAuthority
    @ResponseBody
    public void sendTestEmail(@RequestParam(value = "to") String to) {
        mailService.sendSimpleMessage(to, "TEST EMAIL", "THIS IS A TEST MESSAGE");
    }

}
