package com.java.jdbc;

import com.java.model.Link;
import com.java.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final static String SQL_REMOVE_BY_URL = "DELETE FROM links WHERE url = ?";

    private final static String SQL_GET_BY_URL = "SELECT * FROM links WHERE url = ?";

    private final static String SQL_GET_BY_CHAT_AND_URL =
        """
        SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id
        WHERE links_chats.chat_id = ? AND links.url = ?
        """;

    private final static String SQL_SAVE = "INSERT INTO links (url, last_check, create_at) VALUES (?, ?, ?)";

    private static final String SQL_GETS_CHATS_BY_LAST_CHECK =
        """
        SELECT chat_id FROM links_chats INNER JOIN links ON links_chats.link_id = links.id
        WHERE links.url = ? AND links.last_check < ?
        ORDER BY links.last_check DESC LIMIT 100
        """;

    private static final String SQL_UPDATE = "UPDATE links SET last_check = ? WHERE id = ?";

    private final static String SQL_GETS_BY_LAST_CHECK =
        "SELECT * FROM links WHERE last_check < ?";

    private final static String SQL_EXISTS_BY_URL = "SELECT EXISTS(SELECT * FROM links WHERE url = ?)";

    private final static String SQL_SAVE_LINKS_CHATS = "INSERT INTO links_chats (link_id, chat_id) VALUES (?, ?)";

    private final static String SQL_GETS =
        """
        SELECT * FROM links INNER JOIN links_chats ON links.id = links_chats.link_id WHERE links_chats.chat_id = ?
        """;

    private final static String SQL_EXISTS_LINKS_CHATS = "SELECT EXISTS(SELECT * FROM links_chats WHERE link_id = ?)";

    private final static String SQL_REMOVE_LINKS_CHATS = "DELETE FROM links_chats WHERE link_id = ? AND chat_id = ?";

    private final static String SQL_EXISTS_BY_URL_CHAT_ID =
        """
        SELECT EXISTS(SELECT * FROM links WHERE id IN (SELECT link_id FROM links_chats WHERE chat_id = ?) AND url = ?)
        """;

    private final Connection connection;

    @Override
    public Optional<Link> get(String url) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_URL)) {
            statement.setString(1, url);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return Optional.of(build(result));
            }

        } catch (SQLException exception) {
            log.error("Failed to get link for URL: {}. SQL exception: {}", url, exception.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void remove(String url) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_REMOVE_BY_URL)) {
            statement.setString(1, url);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error("Failed to remove link for URL: {}. SQL exception: {}", url, exception.getMessage());
        }
    }

    @Override
    public Optional<Link> get(String url, Long chatId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_CHAT_AND_URL)) {
            statement.setLong(1, chatId);
            statement.setString(2, url);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return Optional.of(build(result));
            }

        } catch (SQLException exception) {
            log.error(
                "Failed to retrieve link for URL: {}, chat ID: {}. SQL exception: {}",
                url,
                chatId,
                exception.getMessage()
            );
        }

        return Optional.empty();
    }

    @Override
    public void save(String url, Long chatId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
            statement.setString(1, url);
            statement.setObject(2, OffsetDateTime.now());
            statement.setObject(3, OffsetDateTime.now());
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error(
                "Failed to save link for URL: {}, chat ID: {}. SQL exception: {}",
                url,
                chatId,
                exception.getMessage()
            );
        }
    }

    @Override
    public List<Link> gets(Long chatId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GETS)) {
            statement.setLong(1, chatId);
            ResultSet result = statement.executeQuery();
            List<Link> links = new ArrayList<>();

            while (result.next()) {
                links.add(build(result));
            }

            return links;
        } catch (SQLException exception) {
            log.error("Failed to retrieve links for chat ID: {}. SQL exception: {}", chatId, exception.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<Link> getsByLastCheck(Long delay) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GETS_BY_LAST_CHECK)) {
            statement.setLong(1, delay);
            ResultSet result = statement.executeQuery();
            List<Link> links = new ArrayList<>();

            while (result.next()) {
                links.add(build(result));
            }

            return links;
        } catch (SQLException exception) {
            log.error("Failed to retrieve links for last check. SQL exception: {}", exception.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<Long> getsChatByLastCheck(Long delay, String url) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_GETS_CHATS_BY_LAST_CHECK)) {
            statement.setString(1, url);
            statement.setLong(2, delay);
            ResultSet result = statement.executeQuery();
            List<Long> chats = new ArrayList<>();

            while (result.next()) {
                chats.add(result.getLong(1));
            }

            return chats;
        } catch (SQLException exception) {
            log.error("Failed to retrieve chats for last check. SQL exception: {}", exception.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public Boolean existsByUrl(String url) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_URL)) {
            statement.setString(1, url);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }

        } catch (SQLException exception) {
            log.error("Failed to check for URL: {}. SQL exception: {}", url, exception.getMessage());
        }

        return false;
    }

    @Override
    public Boolean existsLinkChat(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_LINKS_CHATS)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }

        } catch (SQLException exception) {
            log.error("Failed to check for ID: {}. SQL exception: {}", id, exception.getMessage());
        }

        return false;
    }

    @Override
    public void updateLinkChat(Long id, Long chatId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LINKS_CHATS)) {
            statement.setLong(1, id);
            statement.setLong(2, chatId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error(
                "Failed to update link-chat for ID: {}, chat ID: {}. SQL exception: {}",
                id,
                chatId,
                exception.getMessage()
            );
        }
    }

    @Override
    public Boolean existsByUrlAndChatId(String url, Long chatId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_BY_URL_CHAT_ID)) {
            statement.setLong(1, chatId);
            statement.setString(2, url);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }

        } catch (SQLException exception) {
            log.error("Failed to check for URL: {}, chat ID: {}. SQL exception: {}", url, chatId, exception);
        }

        return false;
    }

    @Override
    public void removeLinkChat(Long id, Long chatId) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_REMOVE_LINKS_CHATS)) {
            statement.setLong(1, id);
            statement.setLong(2, chatId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error(
                "Failed to remove link-chat. ID: {}, chat ID: {}. SQL exception: {}",
                id,
                chatId,
                exception.getMessage()
            );
        }
    }

    @Override
    public void updateLastCheck(Long id, OffsetDateTime date) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setObject(1, date);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            log.error("Failed to update last check for ID: {}. SQL exception: {}", id, exception.getMessage());
        }
    }

    private Link build(ResultSet result) throws SQLException {
        return Link.builder()
            .id(result.getLong("id"))
            .url(result.getString("url"))
            .lastCheck(result.getObject("last_check", OffsetDateTime.class))
            .createdAt(result.getObject("create_at", OffsetDateTime.class))
            .build();
    }
}
