package com.java.model;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastCheck;
    private OffsetDateTime createdAt;
}
