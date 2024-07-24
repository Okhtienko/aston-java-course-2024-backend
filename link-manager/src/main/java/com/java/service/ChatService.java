package com.java.service;

import com.java.model.Chat;
import com.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repository;

    public void save(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        repository.save(name);
    }

    public void delete(Long id) {
        if (!exists(id)) {
            throw new NoSuchElementException();
        }

        deleteLinkChats(id);
        repository.delete(id);
    }

    public Chat get(Long id) {
        return repository.get(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Chat> gets() {
        return repository.gets();
    }

    private Boolean exists(Long id) {
        return repository.exists(id);
    }

    private void deleteLinkChats(Long id) {
        if (repository.existsLinkChat(id)) {
            repository.deleteLinkChat(id);
        }
    }
}
