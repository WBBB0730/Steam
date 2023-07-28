package com.wbbb.steam.service;

import com.wbbb.steam.dto.response.data.ChatFriendDto;
import com.wbbb.steam.entity.ChatMessage;
import com.wbbb.steam.repository.ChatMessageRepository;
import com.wbbb.steam.repository.FriendRepository;
import com.wbbb.steam.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public List<ChatFriendDto> getChatFriends(Long userId) {
        return chatMessageRepository.getChatFriends(userId);
    }

    public ChatMessage sendChatMessage(Long fromId, Long toId, String content) {
        return chatMessageRepository.save(new ChatMessage(fromId, toId, content));
    }

    public List<ChatMessage> getChatMessages(Long userId, Long friendId, Long id, Integer num) {
        Pageable pageable = PageRequest.of(0, num);
        return id == null ? chatMessageRepository.getChatMessages(userId, friendId, pageable) : chatMessageRepository.getChatMessages(userId, friendId, id, pageable);
    }

    @Transactional
    public void setChatMessagesRead(Long userId, Long friendId, Long id) {
        chatMessageRepository.setChatMessagesRead(userId, friendId, id);
    }
}
