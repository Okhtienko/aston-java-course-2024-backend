package com.java.repository;

import com.java.model.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Optional<Link> get(String url);

    Optional<Link> get(String url, Long chatId);

    List<Link> gets(Long chatId);

    List<Link> getsByLastCheck(Long delay);

    List<Long> getsChatByLastCheck(Long delay, String url);

    Boolean existsByUrl(String url);

    Boolean existsLinkChat(Long id);

    Boolean existsByUrlAndChatId(String url, Long chatId);

    void remove(String url);

    void save(String url, Long chatId);

    void updateLinkChat(Long id, Long chatId);

    void removeLinkChat(Long id, Long chatId);

    void updateLastCheck(Long id, OffsetDateTime date);
}
