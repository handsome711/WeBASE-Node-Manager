package com.webank.webase.node.mgr.base.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.webank.webase.node.mgr.base.tools.NodeMgrTools;

public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private AuthenticationManager authenticationManager;


    public AbstractAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(getHeaderPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        AbstractAuthenticationToken authRequest = buildAuthentication(header);
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        final Authentication authResult;
        try {
            authResult = authenticationManager.authenticate(authRequest);
        } catch (AuthenticationException failed) {
            String errorMessage = failed.getMessage();
            SecurityContextHolder.clearContext();
            //response exception
            NodeMgrTools.responseString(response, errorMessage);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        chain.doFilter(request, response);
    }

    protected abstract String getHeaderPrefix();

    protected abstract AbstractAuthenticationToken buildAuthentication(String header);
}