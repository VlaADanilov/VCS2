package com.technokratos.vcs2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final AutoService autoService;

    public boolean canDelete(UUID autoId, String username) {
        return autoService.isOwner(autoId, username);
    }
}
