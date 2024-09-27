package com.mg.api.smtp.controller;

import com.mg.api.smtp.service.EmailService;
import com.mg.core.common.annotation.MGRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/email")
@MGRestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/emailConfirm")
    public void mailConfirm(String id) {
        int num = emailService.sendEmail(id);
    }
}
