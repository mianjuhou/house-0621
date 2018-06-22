package com.fangdean.house.maper;

import com.fangdean.house.model.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {

    List<City> selectCitys(City city);

}
