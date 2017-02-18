package com.isc.npsd.sharif.model.service;

import com.isc.npsd.common.util.redis.RedisUtil;
import com.isc.npsd.sharif.adapter.SharedObjectsContainer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created by A_Jankeh on 2/18/2017.
 */
@Singleton
@Startup
public class Initializer {

    @PostConstruct
    public void init() {
        initRedisUtil();
    }

    private void initRedisUtil(){
        SharedObjectsContainer.redisUtil = RedisUtil.getInstance();
    }

}
