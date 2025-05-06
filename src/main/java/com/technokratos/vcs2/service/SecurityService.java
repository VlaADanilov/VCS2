package com.technokratos.vcs2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("securityService")
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final AutoService autoService;

    public boolean canDelete(UUID autoId, String username) {
        log.info("Checking if user {} is the owner of auto {}", username, autoId);

        boolean isOwner = autoService.isOwner(autoId, username);

        log.debug("User {} is owner of auto {}: {}", username, autoId, isOwner);
        return isOwner;
    }
}
