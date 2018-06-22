package com.fangdean.house.maper;

import com.fangdean.house.common.request.LimitOffset;
import com.fangdean.house.model.Community;
import com.fangdean.house.model.House;
import com.fangdean.house.model.HouseUser;
import com.fangdean.house.model.UserMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

    int insert(House house);

    List<House> selectHouse(@Param("house") House house, @Param("pageParams") LimitOffset limitOffset);

    Long selectHouseCount(@Param("house") House house);

    List<Community> selectCommunity(Community community);

    int insertUserMsg(UserMsg userMsg);

    int updateHouse(House house);

    HouseUser selectHouseUser(@Param("userId") Long userId, @Param("id") Long id, @Param("type") Integer type);

    HouseUser selectHouseUserById(HouseUser houseUser);

    int insertHouseUser(HouseUser houseUser);

    int downHouse(Long id);

    int deleteHouseUser(@Param("id") Long id, @Param("userId") Long userId, @Param("type") Integer type);
}
