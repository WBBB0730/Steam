package com.wbbb.steam.service;

import com.wbbb.steam.dto.response.data.AppDto;
import com.wbbb.steam.entity.App;
import com.wbbb.steam.repository.AppRepository;
import com.wbbb.steam.util.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class AppService {
    private final AppRepository appRepository;

    /** 创建游戏（TEST） */
    public Long createApp(String name, String cover, String header, List<String> images, String description, String developer, String publisher, double price, double finalPrice, boolean win, boolean mac, boolean linux) {
        App app = new App(null, name, cover, header, String.join(";", images), description, developer, publisher, price, finalPrice, win, mac, linux, System.currentTimeMillis());
        appRepository.save(app);
        return app.getAppId();
    }

    /** 获取游戏详情 */
    public AppDto getApp(Long appId, String token) {
        Long userId = TokenUtil.parseToken(token);
        return appRepository.getApp(appId, userId);
    }

    /** 获取游戏搜索建议 */
    public List<AppDto> getAppSearchSuggestions(String keyword, Integer num, String token) {
        List<String> words = List.of(keyword.toLowerCase().split("\s+"));
        List<AppDto> appList = new ArrayList<>();
        Long userId = TokenUtil.parseToken(token);
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            List<AppDto> temp = appRepository.searchApp(word, userId);
            if (i == 0)
                appList.addAll(temp);
            else
                appList.retainAll(temp);
        }
        if (appList.size() > num)
            appList.subList(0, num);
        return appList;
    }

    /** 获取推荐的游戏 */
    public List<AppDto> getAppRecommendations(Integer num, String token) {
        Sort sort = Sort.by("createTime").descending();
        Pageable pageable = PageRequest.of(0, num, sort);
        Long userId = TokenUtil.parseToken(token);
        return appRepository.getRecommendations(pageable, userId).getContent();
    }

}
