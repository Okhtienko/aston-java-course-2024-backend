package com.java.strategy.chat;

import com.java.service.ChatService;
import com.java.strategy.RequestStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.io.IOException;

@RequiredArgsConstructor
public class DeleteChatStrategy implements RequestStrategy {
    private final ChatService service;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("chatId"));
        service.delete(id);
        response.sendRedirect(request.getContextPath() + "/?action=show");
    }
}
