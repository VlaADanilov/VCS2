package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.ServiceException;
import com.technokratos.vcs2.exception.notFound.UserNotFoundException;
import com.technokratos.vcs2.model.dto.request.EmailForRecoveryRequest;
import com.technokratos.vcs2.model.dto.request.EmailWithCodeAndNewPasswordDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${microservice.url}")
    private String urlOfMyService;
    OkHttpClient client = new OkHttpClient();

    @Override
    public void sendEmail(EmailForRecoveryRequest email) {
        log.info("Initiating password recovery for email: {}", email.getEmail());

        if (!userRepository.existsByEmail(email.getEmail())) {
            log.warn("User with email {} not found", email.getEmail());
            throw new UserNotFoundException(email.getEmail(), true);
        }

        log.info("Creating request to send recovery code to email: {}", email.getEmail());
        createRequest(email);
    }

    private void createRequest(EmailForRecoveryRequest email) {
        StringBuilder url = new StringBuilder(urlOfMyService);
        url.append("/email?email=").append(URLEncoder.encode(email.getEmail(), StandardCharsets.UTF_8));

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        try (Response response = client.newCall(request).execute()) {
            log.debug("Sent HTTP request to URL: {}", url);

            if (!response.isSuccessful()) {
                log.error("Failed to send recovery code. Status code: {}", response.code());
                throw new IOException("Unexpected code " + response.code());
            }

            log.info("Successfully sent recovery code to email: {}", email.getEmail());

        } catch (IOException e) {
            log.error("Error while sending recovery code via HTTP request", e);
            e.printStackTrace();
        }
    }

    @Override
    public void checkResult(EmailWithCodeAndNewPasswordDto dto) {
        log.info("Verifying recovery code for email: {}", dto.getEmail());

        createRequest(dto);
    }

    private void createRequest(EmailWithCodeAndNewPasswordDto dto) {
        StringBuilder url = new StringBuilder(urlOfMyService);
        url.append("/check?email=").append(URLEncoder.encode(dto.getEmail(), StandardCharsets.UTF_8))
                .append("&code=").append(URLEncoder.encode(dto.getCode(), StandardCharsets.UTF_8));

        log.debug("Constructed verification URL: {}", url);

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        try (Response response = client.newCall(request).execute()) {
            log.debug("Sending verification request to URL: {}", url);

            if (!response.isSuccessful()) {
                log.error("Verification failed. HTTP status: {}", response.code());
                throw new IOException("Unexpected code " + response.code());
            }

            String result = response.body().string();
            log.debug("Received response from verification service: {}", result);

            if ("NO".equals(result)) {
                log.warn("Invalid or expired code for email: {}", dto.getEmail());
                throw new ServiceException("Коды не совпадают", HttpStatus.FORBIDDEN);
            } else {
                Optional<User> byEmail = userRepository.getByEmail(dto.getEmail());
                if (byEmail.isEmpty()) {
                    log.warn("User with email {} not found during verification", dto.getEmail());
                    throw new UserNotFoundException(dto.getEmail(), true);
                }

                User user = byEmail.get();
                log.info("Updating password for user: {}", user.getEmail());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                userRepository.save(user);
                log.info("Password successfully updated for user: {}", user.getEmail());
            }

        } catch (IOException e) {
            log.error("Error while verifying recovery code", e);
            e.printStackTrace();
        }
    }
}
