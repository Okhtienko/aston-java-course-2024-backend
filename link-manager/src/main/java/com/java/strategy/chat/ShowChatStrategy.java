package com.java.strategy.chat;

import com.java.model.Chat;
import com.java.service.ChatService;
import com.java.strategy.RequestStrategy;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ShowChatStrategy implements RequestStrategy {
    private final ChatService service;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Chat> chats = service.gets();
        request.setAttribute("chats", chats);
        request.getRequestDispatcher("/chats.jsp").forward(request, response);
    }
}
