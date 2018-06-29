package com.example.demo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    // The request header in which jwt token will be sent
    private String AUTH_HEADER = "ABC";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Get token from request
        String authToken = getToken(request);

        // When token is not provided (either for non-secured endpoint /login or in case when someone tries
        // to access system without token then filter should do nothing (Spring security will guard protected resources against nonauthenticated users)
        if (authToken != null) {
            try {
                // verify token and extract data
                // Jwt jwt = tokenHelperService.verifyAndExtract(authToken)
                // String username = jwt.getClaim("username"); // example (nonexisting method)
                String username = "abc"; //
                // Create user
                UserDetails userDetails = new JWTUserDetails(username);

                System.out.println("logged user: " + userDetails.getUsername());

                // Create authentication
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails, authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // If anyone would like to get information about currently logged user they can use:
                // SecurityContextHolder.getContext().getAuthentication()

            } catch (Exception ex) {
                System.out.println("Authentication failed");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return (new AntPathRequestMatcher("/**/login")).matches(request)
                || (new AntPathRequestMatcher("/health")).matches(request);
    }

    // Assumes the authentication's header value starts with "Bearer " prefix.
    // Can be changed to not use prefix
    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}