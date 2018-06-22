package com.fangdean.user.service;

import com.fangdean.user.common.request.PageParams;
import com.fangdean.user.mapper.AgencyMapper;
import com.fangdean.user.model.Agency;
import com.fangdean.user.model.User;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * 获取所有中介机构
     */
    public List<Agency> getAllAgency() {
        return agencyMapper.select(new Agency());
    }

    /**
     * 获取一个中介机构
     */
    public Agency getAgency(Integer id) {
        Agency query = new Agency();
        query.setId(id);
        List<Agency> agencies = agencyMapper.select(query);
        if (agencies.isEmpty()) {
            return null;
        }
        return agencies.get(0);
    }

    /**
     * 插入一条中介机构数据
     */
    @Transactional(rollbackFor = Exception.class)
    public int add(Agency agency) {
        return agencyMapper.insert(agency);
    }


    /**
     * 获取所有经济人
     */
    public Pair<List<User>, Long> getAllAgent(PageParams pageParams) {
        List<User> agents = agencyMapper.selectAgent(new User(), pageParams);
        setImg(agents);
        Long count = agencyMapper.selectAgentCount(new User());
        return ImmutablePair.of(agents, count);
    }

    /**
     * 获取经济人详情
     */
    public User getAgentDetail(Long id) {
        User agentQuery = new User();
        agentQuery.setId(id);
        agentQuery.setType(2);
        List<User> agents = agencyMapper.selectAgent(agentQuery, new PageParams(1, 1));
        setImg(agents);
        if (!agents.isEmpty()) {
            User agent = agents.get(0);
            Agency agencyQuery = new Agency();
            agencyQuery.setId(agent.getAgencyId());
            List<Agency> agencies = agencyMapper.select(agencyQuery);
            if (!agencies.isEmpty()) {
                agent.setAgencyName(agencies.get(0).getName());
            }
            return agent;
        }
        return null;
    }

    public void setImg(List<User> users) {
        users.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
    }
}
