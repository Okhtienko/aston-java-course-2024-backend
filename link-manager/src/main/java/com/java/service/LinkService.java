package com.java.service;

import com.java.model.Link;
import com.java.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository repository;

    public void save(String url, Long chatId) {
        validateUrlExists(url);

        if (!repository.existsByUrlAndChatId(url, chatId)) {
            repository.save(url, chatId);
            updateLinkChat(url, chatId);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void remove(String url, Long chatId) {
        validateUrlExists(url);
        Long id = getLinkId(url);
        repository.removeLinkChat(id, chatId);

        if (!repository.existsLinkChat(id)) {
            repository.remove(url);
        }
    }

    public List get(String url, Long chatId) {
        validateUrlExists(url);
        validateLinkChatExists(chatId);
        return repository.getsChatByLastCheck(chatId, url);
    }

    public List<Link> gets(Long chatId) {
        return repository.gets(chatId);
    }

    public List<Link> getByLastCheck(Long delay) {
        return repository.getsByLastCheck(delay);
    }

    public List<Long> getsChatByLastCheck(Long delay, String url) {
        validateUrlExists(url);
        return repository.getsChatByLastCheck(delay, url);
    }

    public void updateLastCheck(Long id, OffsetDateTime date) {
        repository.updateLastCheck(id, date);
    }

    public Boolean exists(String url, Long chatId) {
        return repository.existsByUrlAndChatId(url, chatId);
    }

    public void updateLinkChat(String url, Long chatId) {
        Long id = getLinkId(url);
        repository.updateLinkChat(id, chatId);
    }

    private void validateUrlExists(String url) {
        if (!repository.existsByUrl(url)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateLinkChatExists(Long chatId) {
        if (!repository.existsLinkChat(chatId)) {
            throw new IllegalArgumentException();
        }
    }

    private Long getLinkId(String url) {
        Optional<Link> link = repository.get(url);
        return link.map(Link::getId).orElseThrow(IllegalArgumentException::new);
    }
}
