package com.java.strategy.chat;

import com.java.service.ChatService;
import com.java.strategy.RequestStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.io.IOException;

@RequiredArgsConstructor
public class SaveChatStrategy implements RequestStrategy {
    private final ChatService service;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        service.save(name);
        response.sendRedirect(request.getContextPath() + "/?action=showChat");
    }
}
