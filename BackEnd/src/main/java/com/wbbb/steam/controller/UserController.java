package com.wbbb.steam.controller;

import com.wbbb.steam.dto.request.JoinRequestDto;
import com.wbbb.steam.dto.request.LoginRequestDto;
import com.wbbb.steam.dto.response.ResponseDto;
import com.wbbb.steam.dto.response.data.LoginDto;
import com.wbbb.steam.dto.response.data.UserInfoDto;
import com.wbbb.steam.entity.User;
import com.wbbb.steam.service.UserService;
import com.wbbb.steam.util.ValidateUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ResponseDto<LoginDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginDto loginDto = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (loginDto == null)
            return ResponseDto.notFound(null, "请核对您的密码和帐户名称并重试。");
        return ResponseDto.success(loginDto, "登录成功");
    }

    /**
     * 获取账户信息
     */
    @GetMapping("/info")
    public ResponseDto<UserInfoDto> getUserInfo(@RequestParam(value = "userId", required = false) Long userId, @RequestHeader(value = "token", required = false) String token) {
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
        return ResponseDto.success(new UserInfoDto(userId, user.getUsername(), user.getNickname(), user.getAvatar()));
    }
}
