package com.drufontael.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.drufontael.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var servletPath=request.getServletPath();
        if(servletPath.startsWith("/tasks")) {
            var authorization = request.getHeader("Authorization");
            var authoCode = authorization.split(" ")[1].trim();
            byte[] authoDecode = Base64.getDecoder().decode(authoCode);
            var authoString = new String(authoDecode);
            var username = authoString.split(":")[0];
            var password = authoString.split(":")[1];
            var user = userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuario não autorizado!");
            } else {
                var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerified.verified) {
                    request.setAttribute("userId",user.getId());
                    filterChain.doFilter(request, response);
                } else response.sendError(401, "Senha incorreta!");
            }
        }else{
            filterChain.doFilter(request,response);
        }
    }
}
