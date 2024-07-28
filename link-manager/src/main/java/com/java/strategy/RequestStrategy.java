package com.java.strategy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RequestStrategy {
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
