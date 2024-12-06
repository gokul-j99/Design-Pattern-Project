package com.stock.management.interceptors;

import com.stock.management.storage.InMemoryDatabase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
public class UserValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = request.getParameter("username");

        if (!InMemoryDatabase.isValidUser(username)) {
            System.out.println(username);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Permission denied: User is not valid or logged in.");
            return false; // Stop the request from proceeding
        }

        return true; // Proceed with the request
    }
}
