package com.java.jdbc;

import com.java.model.Chat;
import com.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private static final String SQL_GETS = "SELECT * FROM chats";

    private static final String SQL_GET = "SELECT * FROM chats WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM chats WHERE id = ?";

    private static final String SQL_EXISTS = "SELECT EXISTS(SELECT * FROM chats WHERE id = ?)";

    private static final String SQL_DELETE_LINK_CHAT = "DELETE FROM links_chats WHERE chat_id = ?";

    private static final String SQL_SAVE = "INSERT INTO chats (name) VALUES (?)";

    private static final String SQL_EXISTS_IN_LINK_CHAT = "SELECT EXISTS(SELECT * FROM links_chats WHERE chat_id = ?)";

    private final Connection connection;

    @Override
    public void save(String name) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error("Failed to save chat. Chat name: {}. SQL exception: {}", name, exception.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error("Failed to delete chat with ID: {}. SQL exception: {}", id, exception.getMessage());
        }
    }

    @Override
    public List<Chat> gets() {
        try(PreparedStatement statement = connection.prepareStatement(SQL_GETS)) {
            ResultSet result = statement.executeQuery();
            List<Chat> chats = new ArrayList<>();

            while (result.next()) {
                chats.add(build(result));
            }

            return chats;

        } catch (SQLException exception) {
            log.error("Failed to get chats. SQL exception: {}", exception.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<Chat> get(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GET)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return Optional.of(build(result));
            }

        } catch (SQLException exception) {
            log.error("Failed to get chat with ID: {}. SQL exception: {}", id, exception.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Boolean exists(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS)){
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }

        } catch (SQLException exception) {
            log.error("Failed to check if chat exists with ID: {}. SQL exception: {}", id, exception.getMessage());
        }

        return false;
    }

    @Override
    public Boolean existsLinkChat(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_IN_LINK_CHAT)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }

        } catch (SQLException exception) {
            log.error(
                "Failed to check if chat exists in link_chats with ID: {}. SQL exception: {}",
                id,
                exception.getMessage()
            );
        }
        return false;
    }

    @Override
    public void deleteLinkChat(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_LINK_CHAT)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error("Failed to unlink chat with ID: {}. SQL exception: {}", id, exception.getMessage());
        }
    }

    private Chat build(ResultSet result) throws SQLException {
        return Chat.builder()
            .id(result.getLong("id"))
            .name(result.getString("name"))
            .build();
    }
}
