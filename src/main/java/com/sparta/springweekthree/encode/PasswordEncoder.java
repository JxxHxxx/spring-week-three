package com.sparta.springweekthree.encode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class PasswordEncoder {

    private final MessageDigest messageDigest;

    {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.info("존재하지 않는 암호화 알고리즘입니다.");
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String confidential) {
        messageDigest.update(confidential.getBytes());
        byte[] bytes = messageDigest.digest();

        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }
}
