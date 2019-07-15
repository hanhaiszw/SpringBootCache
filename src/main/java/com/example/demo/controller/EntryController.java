package com.example.demo.controller;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * create by sunzhongwei on 2019/07/09
 * Desc:
 **/

@EnableScheduling
@RestController
@CacheConfig(cacheNames = "testCache")
public class EntryController {

    @RequestMapping("/")
    private String index() {
        return "Spring Cache Test";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    private String get(@PathVariable int id) {
        return cacheTest(id);
    }


    // 不缓存null结果
    @Cacheable(key = "#num", unless = "#result==null")
    public String cacheTest(int num) {
        System.out.println("测试cache " + num);
        return "测试cache " + num;
    }


    //
    @Cacheable(key = "#root.methodName", unless = "#result==null")
    public String cacheTest2() {
        System.out.println("测试cache2 ");
        return "测试cache2 ";
    }


    // @EnableScheduling 定时任务必需注解
    // 每2分钟清理一下缓存
    // beforeInvocation在此方法执行前清除缓存
    @Scheduled(fixedDelay = 2 * 60 * 1000)
    @CacheEvict(value = "testCache", allEntries = true, beforeInvocation = true)
    public void cleanCache() {
        System.out.println("evict order_list_t1 cache at time = " + LocalDateTime.now());
    }

}
