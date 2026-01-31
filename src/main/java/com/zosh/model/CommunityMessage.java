// src/main/java/com/zosh/model/CommunityMessage.java
package com.zosh.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class CommunityMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User sender;

    private String message;
    private LocalDateTime timestamp;
}