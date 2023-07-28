package com.wbbb.steam.controller;

import com.wbbb.steam.dto.request.SortWishlistRequestDto;
import com.wbbb.steam.dto.response.ResponseDto;
import com.wbbb.steam.dto.response.data.WishlistItemDto;
import com.wbbb.steam.service.UserService;
import com.wbbb.steam.service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/wishlist")
@CrossOrigin("*")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;

    @GetMapping
    public ResponseDto<List<WishlistItemDto>> getWishList(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(wishlistService.getWishlist(userId));
    }

    @GetMapping("/size")
    public ResponseDto<Integer> getWishlistSize(@RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        return ResponseDto.success(wishlistService.getWishlistSize(userId));
    }

    @PostMapping
    public ResponseDto<?> addAppToWishlist(
            @RequestParam("appId") Long appId,
            @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        int code = wishlistService.addAppToWishlist(userId, appId);
        if (code == 200)
            return ResponseDto.success(null);
        if (code == 404)
            return ResponseDto.notFound(null);
        if (code == 409)
            return ResponseDto.conflict(null, "游戏已在愿望单中");
        return ResponseDto.serverError(null);
    }

    @DeleteMapping
    public ResponseDto<?> removeAppFromWishlist(
            @RequestParam("appId") Long appId,
            @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        int code = wishlistService.removeAppFromWishlist(userId, appId);
        if (code == 200)
            return ResponseDto.success(null);
        if (code == 404)
            return ResponseDto.notFound(null, "愿望单中未找到该游戏");
        return ResponseDto.serverError(null);
    }

    @PutMapping("/sort")
    public ResponseDto<?> sortWishlist(@RequestBody List<SortWishlistRequestDto> list, @RequestHeader("token") String token) {
        Long userId = userService.parseToken(token);
        if (userId == null)
            return ResponseDto.unauthorized(null, "登录状态已失效，请重新登录");
        wishlistService.sortWishlist(userId, list);
        return ResponseDto.success(null);
    }
}
