package com.fangdean.user.mapper;

import com.fangdean.user.common.request.PageParams;
import com.fangdean.user.model.Agency;
import com.fangdean.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgencyMapper {

    List<Agency> select(Agency agency);

    int insert(Agency agency);

    List<User> selectAgent(@Param("user") User user, @Param("pageParams") PageParams pageParams);

    Long selectAgentCount(@Param("user") User user);

}
