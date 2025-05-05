package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.PasswordRecoveryApi;
import com.technokratos.vcs2.model.dto.request.EmailForRecoveryRequest;
import com.technokratos.vcs2.model.dto.request.EmailWithCodeAndNewPasswordDto;
import com.technokratos.vcs2.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class PasswordRecoveryController implements PasswordRecoveryApi {
    private final EmailService emailService;
    @Override
    public String getPasswordRecoveryForm(Model model) {
        model.addAttribute("back","/login");
        return "password_recovery_form";
    }

    @Override
    public void sendEmail(EmailForRecoveryRequest email) {
        emailService.sendEmail(email);
    }

    @Override
    public void recovery(EmailWithCodeAndNewPasswordDto email) {
        emailService.checkResult(email);
    }
}
