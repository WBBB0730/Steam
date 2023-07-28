package com.wbbb.steam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "from_id")
    private Long fromId;

    @Column(name = "to_id")
    private Long toId;

    @Column(length = 4095)
    private String content;

    private boolean isRead;

    @Column(name = "create_time")
    private Long createTime;

    public ChatMessage(Long fromId, Long toId, String content) {
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
        this.isRead = false;
        this.createTime = System.currentTimeMillis();
    }
}
