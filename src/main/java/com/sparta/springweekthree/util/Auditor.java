package com.sparta.springweekthree.util;

import com.sparta.springweekthree.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class Auditor implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 기본 값이 anonymousUser String
        if (principal instanceof UserDetailsImpl) {
            Long id = ((UserDetailsImpl) principal).getMember().getId();
            return Optional.of(id);
        }

        return Optional.empty();
    }
}
