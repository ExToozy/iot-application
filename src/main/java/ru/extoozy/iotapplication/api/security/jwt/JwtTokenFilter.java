package ru.extoozy.iotapplication.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String token = getTokenFromRequest(request);

            if (token.isEmpty() || jwtTokenProvider.isExpiredToken(token)) {
                chain.doFilter(request, response);
                return;
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            if (authentication == null) {
                chain.doFilter(request, response);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ignored) {
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }
}
