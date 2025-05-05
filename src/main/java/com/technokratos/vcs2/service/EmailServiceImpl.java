package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.ServiceException;
import com.technokratos.vcs2.exception.notFound.UserNotFoundException;
import com.technokratos.vcs2.model.dto.request.EmailForRecoveryRequest;
import com.technokratos.vcs2.model.dto.request.EmailWithCodeAndNewPasswordDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${microservice.url}")
    private String urlOfMyService;
    OkHttpClient client = new OkHttpClient();

    @Override
    public void sendEmail(EmailForRecoveryRequest email) {
        if (!userRepository.existsByEmail(email.getEmail())) {
            throw new UserNotFoundException(email.getEmail(), true);
        }
        createRequest(email);
    }

    private void createRequest(EmailForRecoveryRequest email) {
        StringBuilder url = new StringBuilder(urlOfMyService);
        url.append("/email?email=").append(URLEncoder.encode(email.getEmail(), StandardCharsets.UTF_8));

        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response.code());
            }
            System.out.println("Успешно отправлен код на почту %s".formatted(email.getEmail()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkResult(EmailWithCodeAndNewPasswordDto email) {
        createRequest(email);
    }

    private void createRequest(EmailWithCodeAndNewPasswordDto email) {
        StringBuilder url = new StringBuilder(urlOfMyService);
        url.append("/check?email=").append(URLEncoder.encode(email.getEmail(), StandardCharsets.UTF_8));
        url.append("&code=").append(URLEncoder.encode(email.getCode(), StandardCharsets.UTF_8));
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response.code());
            }
            String result = response.body().string();
            if (result.equals("NO")) {
                throw new ServiceException("Коды не совпадают", HttpStatus.FORBIDDEN);
            } else {
                Optional<User> byEmail = userRepository.getByEmail(email.getEmail());
                if (byEmail.isEmpty()) {
                    throw new UserNotFoundException(email.getEmail(), true);
                } else {
                    byEmail.get().setPassword(passwordEncoder.encode(email.getPassword()));
                    userRepository.save(byEmail.get());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
