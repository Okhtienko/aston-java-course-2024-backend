package com.java.listener;

import com.java.jdbc.JdbcChatRepository;
import com.java.jdbc.JdbcLinkRepository;
import com.java.repository.ChatRepository;
import com.java.repository.LinkRepository;
import com.java.service.ChatService;
import com.java.service.LinkService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@WebListener
public class DependencyInitializationContextListener implements ServletContextListener {
    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initializeDatabaseConnection(event.getServletContext());
        initializeServices(event.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            connection = (Connection) event.getServletContext().getAttribute("connection");
            connection.close();
        } catch (SQLException exception) {
            log.error("Failed to close connection. SQL exception: {}", exception.getMessage());
        }
    }

    private void initializeDatabaseConnection(ServletContext context) {
        String driver = context.getInitParameter("driver");
        String username = context.getInitParameter("user");
        String password = context.getInitParameter("password");
        String url = context.getInitParameter("url");

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception exception) {
            log.error("Failed to initialize database connection. SQL exception: {}", exception.getMessage());
        }
    }

    private void initializeServices(ServletContext context) {
        try {
            ChatRepository chatRepository = new JdbcChatRepository(connection);
            ChatService chatService = new ChatService(chatRepository);
            context.setAttribute("chatService", chatService);

            LinkRepository linkRepository = new JdbcLinkRepository(connection);
            LinkService linkService = new LinkService(linkRepository);
            context.setAttribute("linkService", linkService);
        } catch (Exception exception) {
            log.error("Failed to initialize services. Exception: {}", exception.getMessage());
        }
    }
}
