package com.java.repository;

import com.java.model.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    void save(String name);

    void delete(Long id);

    void deleteLinkChat(Long id);

    List<Chat> gets();

    Optional<Chat> get(Long id);

    Boolean exists(Long id);

    Boolean existsLinkChat(Long id);
}
