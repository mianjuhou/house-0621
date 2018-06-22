package com.fangdean.house.service;

import com.fangdean.house.common.request.LimitOffset;
import com.fangdean.house.model.House;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    private static final String HOT_HOUSE_KEY = "_hot_house";

    @Autowired
    private HouseService houseService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<House> getHotHouse(Integer size) {
        Set<String> idSet = redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, -1);//bug修复，根据热度从高到底排序
        List<Long> ids = idSet.stream().map(b -> Long.parseLong(b)).collect(Collectors.toList());
        House query = new House();
        query.setIds(ids);
        return houseService.queryAndSetImg(query, LimitOffset.build(size, 0));
    }

    public void increaseHot(long id) {
        redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY, "" + id, 1.0D);
        redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY, 0, -11);
    }

    public List<House> getLastest() {
        House query = new House();
        query.setSort("create_time");
        return houseService.queryAndSetImg(query, LimitOffset.build(8, 0));
    }

}
