package com.wbbb.steam.controller;

import com.wbbb.steam.dto.request.JoinRequestDto;
import com.wbbb.steam.dto.request.LoginRequestDto;
import com.wbbb.steam.dto.response.PageDto;
import com.wbbb.steam.dto.response.ResponseDto;
import com.wbbb.steam.dto.response.data.LoginResponseDto;
import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.dto.response.data.UserInfoResponseDto;
import com.wbbb.steam.entity.User;
import com.wbbb.steam.service.UserService;
import com.wbbb.steam.util.ValidateUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /**
     * 检查账户名称是否可用
     */
    @GetMapping("/available")
    public ResponseDto<Boolean> checkUsernameAvailable(@RequestParam("username") String username) {
        if (username.isEmpty())
            return ResponseDto.success(false);
        return ResponseDto.success(userService.isUsernameAvailable(username));
    }

    /**
     * 注册
     */
    @PostMapping("/join")
    public ResponseDto<?> join(@RequestBody JoinRequestDto joinRequestDto) {
        if (!ValidateUtil.isEmailValid(joinRequestDto.getEmail()))
            return ResponseDto.badRequest(null, "请输入有效的电子邮件地址");
        if (!userService.isUsernameAvailable(joinRequestDto.getUsername()))
            return ResponseDto.conflict(null, "账户名称不可用");
        userService.join(joinRequestDto.getEmail(), joinRequestDto.getUsername(), joinRequestDto.getPassword());
        return ResponseDto.success(null, "注册成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseDto<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (loginResponseDto == null)
            return ResponseDto.notFound(null, "请核对您的密码和帐户名称并重试。");
        return ResponseDto.success(loginResponseDto, "登录成功");
    }

    /**
     * 获取账户信息
     */
    @GetMapping("/info")
    public ResponseDto<UserInfoResponseDto> getUserInfo(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestHeader(value = "token", required = false) String token) {
        if (userId == null && token == null)
            return ResponseDto.badRequest(null, "请求参数错误");
        Long tokenUserId;
        if (userId == null) {
            tokenUserId = userService.parseToken(token);
            if (tokenUserId == null)
                return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
            userId = tokenUserId;
        }
        User user = userService.getUser(userId);
        if (user == null)
            return ResponseDto.notFound(null, "无法找到指定的个人资料");
        return ResponseDto.success(new UserInfoResponseDto(userId, user.getUsername(), user.getNickname(), user.getAvatar()));
    }

    @GetMapping("/search")
    public ResponseDto<PageDto<UserDto>> searchUser(
            @RequestParam("keyword") String keyword,
            @RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize,
            @RequestHeader("token") String token) {
        if (keyword.isEmpty())
            return ResponseDto.success(new PageDto<>(0L, pageIndex, pageSize, new ArrayList<>()));
        return ResponseDto.success(userService.searchUser(keyword, pageIndex, pageSize, token));
    }
}
