package com.grinnotech.patients.service;

import com.grinnotech.patients.dao.authorities.RequireAdminAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemService {

    @Autowired
    private MailService mailService;

    @RequestMapping(path = "/sendtestemail", method = RequestMethod.GET)
    @RequireAdminAuthority
    @ResponseBody
    public void sendTestEmail(@RequestParam(value = "to") String to) {
        this.mailService.sendSimpleMessage(to, "TEST EMAIL", "THIS IS A TEST MESSAGE");
    }

}