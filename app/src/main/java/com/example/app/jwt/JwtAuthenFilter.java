package com.example.app.jwt;
//
//import com.example.app.service.UserDetailsServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class JwtAuthenFilter extends OncePerRequestFilter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    private final RequestMatcher uriMatcher1 = new AntPathRequestMatcher("/auth/v1/**", HttpMethod.GET.name());
    private final RequestMatcher uriMatcher2 = new AntPathRequestMatcher("/auth/v1/**", HttpMethod.POST.name());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filter All The Request....");
        String bearerToken = request.getHeader("Authorization");
        String jwt = "";
        if(!bearerToken.isEmpty()  && !bearerToken.isBlank() && bearerToken.startsWith("Bearer")){
            jwt = bearerToken.substring(7);
        }
        boolean isValidJwtToken  = jwtService.validateJwtToken(jwt);
        if(isValidJwtToken) {
            String username = JWT.decode(jwt).getIssuer().toString();
            Optional<User> user = userRepository.findByUsername(username);

            System.out.println(user.get());
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetailsService.loadUserByUsername(user.get().getUsername()), null, userDetailsService.loadUserByUsername(username).getAuthorities());

            /**  valid the jwt token if it is valid then SecurityContextHolder can
             *   set context and filter will not filter this request
             *   otherwhise jwt token is invalid then it will throw exception ,
             *   then SecurityContextHolder can not set context
             *   then filter will not see context then will filter this request
             */

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);

    }

    //without this method . JwtAuthenFilter will by pass all the request .Event the mathchers
     @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return uriMatcher1.matches(request) || uriMatcher2.matches(request);
    }
}
