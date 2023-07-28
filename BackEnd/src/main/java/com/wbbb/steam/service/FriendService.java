package com.wbbb.steam.service;

import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.entity.Friend;
import com.wbbb.steam.entity.Invitation;
import com.wbbb.steam.repository.FriendRepository;
import com.wbbb.steam.repository.InvitationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final InvitationRepository invitationRepository;

    /** 获取好友列表 */
    public List<UserDto> getFriendList(Long userId) {
        return friendRepository.getFriendList(userId);
    }

    /** 获取好友数量 */
    public int getFriendNum(Long userId) {
        return friendRepository.countAllByUserId(userId);
    }

    /** 发送好友申请 */
    public boolean sendFriendInvitation(Long userId, Long friendId) {
        if (friendRepository.existsByUserIdAndFriendId(userId, friendId) || invitationRepository.existsByUserIdAndFriendId(userId, friendId))
            return false;
        invitationRepository.save(new Invitation(userId, friendId, System.currentTimeMillis()));
        return true;
    }

    /** 取消好友申请 */
    @Transactional
    public boolean cancelFriendInvitation(Long userId, Long friendId) {
        if (!invitationRepository.existsByUserIdAndFriendId(userId, friendId))
            return false;
        invitationRepository.deleteByUserIdAndFriendId(userId, friendId);
        return true;
    }

    /** 接受好友申请 */
    @Transactional
    public boolean acceptFriendInvitation(Long userId, Long friendId) {
        if (!invitationRepository.existsByUserIdAndFriendId(friendId, userId) || friendRepository.existsByUserIdAndFriendId(userId, friendId))
            return false;
        Long time = System.currentTimeMillis();
        friendRepository.save(new Friend(userId, friendId, time));
        friendRepository.save(new Friend(friendId, userId, time));
        invitationRepository.deleteByUserIdAndFriendId(userId, friendId);
        invitationRepository.deleteByUserIdAndFriendId(friendId, userId);
        return true;
    }

    /** 拒绝好友申请 */
    @Transactional
    public boolean refuseFriendInvitation(Long userId, Long friendId) {
        if (!invitationRepository.existsByUserIdAndFriendId(friendId, userId))
            return false;
        invitationRepository.deleteByUserIdAndFriendId(friendId, userId);
        return true;
    }

    /** 获取已发送的好友申请 */
    public List<UserDto> getSentFriendInvitations(Long userId) {
        return invitationRepository.getSentFriendInvitations(userId);
    }


    /** 获取未处理的好友申请 */
    public List<UserDto> getPendingFriendInvitations(Long userId) {
        return invitationRepository.getPendingFriendInvitations(userId);
    }

    /** 获取未处理的好友申请数量 */
    public Long getPendingFriendInvitationNum(Long userId) {
        return invitationRepository.countByFriendId(userId);
    }

    public Boolean isFriend(Long userId, Long friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }
}
