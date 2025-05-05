package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.request.EmailForRecoveryRequest;
import com.technokratos.vcs2.model.dto.request.EmailWithCodeAndNewPasswordDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface EmailService {
    void sendEmail(@Valid EmailForRecoveryRequest email);

    void checkResult(@Valid EmailWithCodeAndNewPasswordDto email);
}
