package com.fangdean.house.service;

import com.fangdean.house.maper.CityMapper;
import com.fangdean.house.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    public List<City> getAllCitys() {
        return cityMapper.selectCitys(new City());
    }

}
