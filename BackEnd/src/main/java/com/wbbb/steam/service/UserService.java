package com.wbbb.steam.service;

import com.wbbb.steam.dto.response.PageDto;
import com.wbbb.steam.dto.response.data.LoginResponseDto;
import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.entity.User;
import com.wbbb.steam.repository.UserRepository;
import com.wbbb.steam.util.CryptoUtil;
import com.wbbb.steam.util.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * 登录
     * @param username 账户名称
     * @param password 密码
     * @return 登录成功返回token，失败返回null
     */
    public LoginResponseDto login(String username, String password) {
        password = CryptoUtil.SHA256(password);
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password))
            return null;
        return new LoginResponseDto(TokenUtil.generateToken(user), user.getUserId());
    }

    /**
     * 注册
     * @param email    电子邮箱地址
     * @param username 账户名称
     * @param password 密码
     */
    public void join(String email, String username, String password) {
        password = CryptoUtil.SHA256(password);
        User user = new User(email, username, password, username, "https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets/default_avatar.jpg", System.currentTimeMillis());
        userRepository.save(user);
    }

    /**
     * 检测账户名称是否可用
     * @param username 账户名称
     * @return boolean
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * 获取账户信息
     * @param userId 账户id
     */
    public User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    /**
     * 搜索用户
     * @param keyword 搜索关键词，匹配昵称、用户名、id
     * @param token   Token
     * @return 用户列表
     */
    public PageDto<UserDto> searchUser(String keyword, Integer pageIndex, Integer pageSize, String token) {
        Long userId = TokenUtil.parseToken(token);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<UserDto> page = userRepository.searchUser(keyword.trim(), userId, pageable);
        return new PageDto<>(page.getTotalElements(), page.getNumber() + 1, pageSize, page.getContent());
    }

    /**
     * 解析Token，并验证userId是否存在
     * @param token Token
     * @return 成功返回userId，失败返回null
     */
    public Long parseToken(String token) {
        Long userId = TokenUtil.parseToken(token);
        if (userId == null || !userRepository.existsById(userId))
            return null;
        return userId;
    }

    /**
     * 验证userId是否存在
     * @param userId userId
     * @return boolean
     */
    public boolean isUserIdExists(Long userId) {
        return userId != null && userRepository.existsById(userId);
    }
}
