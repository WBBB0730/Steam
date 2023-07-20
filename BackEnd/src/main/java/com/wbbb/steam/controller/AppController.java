package com.wbbb.steam.controller;

import com.wbbb.steam.dto.request.CreateAppRequestDto;
import com.wbbb.steam.dto.response.ResponseDto;
import com.wbbb.steam.dto.response.data.AppDto;
import com.wbbb.steam.service.AppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/app")
public class AppController {
    private final AppService appService;

    /** 创建游戏 */
    @PostMapping
    public ResponseDto<Long> createApp(@RequestBody CreateAppRequestDto createAppRequestDto) {
        String name, cover, header, description, developer, publisher;
        List<String> images;
        double price, finalPrice;
        boolean win, mac, linux;
        try {
            name = createAppRequestDto.getName();
            cover = createAppRequestDto.getCover();
            header = createAppRequestDto.getHeader();
            images = createAppRequestDto.getImages();
            description = createAppRequestDto.getDescription();
            developer = createAppRequestDto.getDeveloper();
            publisher = createAppRequestDto.getPublisher();
            price = createAppRequestDto.getPrice();
            finalPrice = createAppRequestDto.getFinalPrice();
            win = createAppRequestDto.getWin();
            mac = createAppRequestDto.getMac();
            linux = createAppRequestDto.getLinux();
        } catch (Exception e) {
            return ResponseDto.badRequest(null, "请求参数错误");
        }
        Long appId = appService.createApp(name, cover, header, images, description, developer, publisher,
                price, finalPrice, win, mac, linux);
        return ResponseDto.success(appId, "创建成功");
    }

    /** 获取游戏详情 */
    @GetMapping
    public ResponseDto<AppDto> getApp(
            @RequestParam("appId") Long appId,
            @RequestHeader(value = "token", required = false) String token) {
        AppDto app = appService.getApp(appId, token);
        if (app == null)
            return ResponseDto.notFound(null);
        return ResponseDto.success(app);
    }

    /** 获取游戏搜索建议 */
    @GetMapping("/search/suggestions")
    public ResponseDto<List<AppDto>> getAppSearchSuggestions(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "num", defaultValue = "5") Integer num,
            @RequestHeader(value = "token", required = false) String token) {
        return ResponseDto.success(appService.getAppSearchSuggestions(keyword, num, token));
    }

    /** 获取游戏推荐列表 */
    @GetMapping("/recommendations")
    public ResponseDto<List<AppDto>> getAppRecommendations(
            @RequestParam(value = "num", defaultValue = "12") Integer num,
            @RequestHeader(value = "token", required = false) String token) {
        return ResponseDto.success(appService.getAppRecommendations(num, token));
    }
}
