package com.wbbb.steam.controller;

import com.wbbb.steam.dto.response.ResponseDto;
import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.service.FriendService;
import com.wbbb.steam.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    /** 获取好友列表 */
    @GetMapping("/list")
    public ResponseDto<List<UserDto>> getFriendList(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(friendService.getFriendList(userId));
    }

    /** 获取好友数量 */
    @GetMapping("/num")
    public ResponseDto<Integer> getFriendNum(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(friendService.getFriendNum(userId));
    }

    /** 发送好友申请 */
    @PostMapping("/invite")
    public ResponseDto<?> sendFriendInvitation(@RequestParam("userId") Long friendId, @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        if (userId.equals(friendId))
            return ResponseDto.conflict(null, "不能添加自己为好友");
        if (!userService.isUserIdExists(friendId))
            return ResponseDto.notFound(null, "用户不存在");
        if(!friendService.sendFriendInvitation(userId, friendId))
            return ResponseDto.conflict(null);
        return ResponseDto.success(null);
    }

    /** 取消好友申请 */
    @PostMapping("/cancel")
    public ResponseDto<?> cancelFriendInvitation(@RequestParam("userId") Long friendId, @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        if (!friendService.cancelFriendInvitation(userId, friendId))
            return ResponseDto.notFound(null);
        return ResponseDto.success(null);
    }

    /** 接收好友申请 */
    @PostMapping("/accept")
    public ResponseDto<?> acceptFriendInvitation(@RequestParam("userId") Long friendId, @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        if (userId.equals(friendId))
            return ResponseDto.conflict(null, "不能添加自己为好友");
        if (!userService.isUserIdExists(friendId))
            return ResponseDto.notFound(null, "用户不存在");
        if (!friendService.acceptFriendInvitation(userId, friendId))
            return ResponseDto.notFound(null);
        return ResponseDto.success(null);
    }

    /** 拒绝好友申请 */
    @PostMapping("/refuse")
    public ResponseDto<?> refuseFriendInvitation(@RequestParam("userId") Long friendId, @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        if (!friendService.refuseFriendInvitation(userId, friendId))
            return ResponseDto.notFound(null);
        return ResponseDto.success(null);
    }

    /** 获取已发送的好友申请 */
    @GetMapping("/invite")
    public ResponseDto<List<UserDto>> getSentFriendInvitations(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(friendService.getSentFriendInvitations(userId));
    }

    /** 获取未处理的好友申请 */
    @GetMapping("/pending")
    public ResponseDto<List<UserDto>> getPendingFriendInvitations(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(friendService.getPendingFriendInvitations(userId));
    }

    /** 获取未处理的好友申请数量 */
    @GetMapping("/pending/num")
    public ResponseDto<Long> getPendingFriendInvitationNum(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(friendService.getPendingFriendInvitationNum(userId));
    }

}
