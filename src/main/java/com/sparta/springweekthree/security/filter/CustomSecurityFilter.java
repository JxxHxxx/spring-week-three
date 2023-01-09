package com.sparta.springweekthree.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springweekthree.security.MemberDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomSecurityFilter extends OncePerRequestFilter {

    private final MemberDetailsServiceImpl memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

//        ServletInputStream inputStream = request.getInputStream();
//        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//
//        SignUpRequestDto member = objectMapper.readValue(messageBody, SignUpRequestDto.class);
//
//        log.info("[SignUpRequestDto username = {}]", member.getUsername());
//        log.info("[SignUpRequestDto password = {}]", member.getPassword());

        log.info("[username = {}]", username);
        log.info("[password = {}]", password);
        log.info("[request.getRequestURI() = {}]", request.getRequestURI());

        if(username != null && password  != null && (request.getRequestURI().equals("/auth/login"))){
            UserDetails userDetails = memberDetailsService.loadUserByUsername(username);

            // 비밀번호 확인
            if(!passwordEncoder.matches(password, userDetails.getPassword())) {
                log.info("CustomSecurityFilter - 비밀번호 체크");
                throw new IllegalAccessError("비밀번호가 일치하지 않습니다.");
            }

            // 인증 객체 생성 및 등록
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request,response);
    }
}