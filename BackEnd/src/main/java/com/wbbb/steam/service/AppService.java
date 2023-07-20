package com.wbbb.steam.service;

import com.wbbb.steam.dto.response.data.AppDto;
import com.wbbb.steam.entity.App;
import com.wbbb.steam.repository.AppRepository;
import com.wbbb.steam.repository.WishlistRepository;
import com.wbbb.steam.util.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AppService {
    private final AppRepository appRepository;

    private final WishlistRepository wishlistRepository;

    // TEST

    /**
     * 创建游戏
     */
    public Long createApp(String name, String cover, String header, List<String> images, String description, String developer, String publisher, double price, double finalPrice, boolean win, boolean mac, boolean linux) {
        App app = new App(null, name, cover, header, String.join(";", images), description, developer, publisher, price, finalPrice, win, mac, linux, System.currentTimeMillis());
        appRepository.save(app);
        return app.getAppId();
    }

    /**
     * 获取游戏详情
     */
    public AppDto getApp(Long appId, String token) {
        App app = appRepository.findById(appId).orElse(null);
        if (app == null)
            return null;
        Long userId = TokenUtil.parseToken(token);
        return toAppDto(app, userId);
    }

    /**
     * 获取游戏搜索建议
     */
    public List<AppDto> getAppSearchSuggestions(String keyword, Integer num, String token) {
        List<String> words = List.of(keyword.split("\s+"));
        List<App> appList = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            List<App> temp = appRepository.findAllByNameContainingIgnoreCaseOrDeveloperContainingIgnoreCaseOrPublisherContainingIgnoreCase(word, word, word);
            if (i == 0)
                appList.addAll(temp);
            else
                appList.retainAll(temp);
        }
        Long userId = TokenUtil.parseToken(token);
        if (appList.size() > num)
            appList.subList(0, num);
        return toAppDtoList(appList, userId);
    }

    /**
     * 获取推荐的游戏
     */
    public List<AppDto> getAppRecommendations(Integer num, String token) {
        Sort sort = Sort.by("createTime").descending();
        Pageable pageable = PageRequest.of(0, num, sort);
        List<App> appList = appRepository.findAll(pageable).getContent();
        Long userId = TokenUtil.parseToken(token);
        return toAppDtoList(appList, userId);
    }

    /**
     * 将App转换为AppDto
     */
    public AppDto toAppDto(App app, Long userId) {
        AppDto appDto = new AppDto(app);
        if (userId != null) {
            if (wishlistRepository.existsByUserIdAndAppId(userId, app.getAppId()))
                appDto.setStatus(1);
        }
        return appDto;
    }

    /**
     * 将List[App]转换为List[AppDto]
     */
    public List<AppDto> toAppDtoList(List<App> appList, Long userId) {
        return appList.stream()
                .map(app -> toAppDto(app, userId))
                .collect(Collectors.toList());
    }
}
