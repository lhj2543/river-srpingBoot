package com.river.test.controller;


import com.river.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页 前端控制器
 *
 * @author river
 * @since 2020-09-01
 */
@RestController
@RequestMapping("/public")
@Slf4j
@Api(value = "index",tags="站点首页管理")
public class PublicController {


    /**
     * 首页数据初始化
     * @return
     */
    @RequestMapping("/index")
    @ApiOperation(value = "initData", httpMethod = "GET", response = Result.class, notes = "站点首页数据初始化")
    public Result initData(){

        log.info("加载首页数据开始");
        try {


        } catch(Exception e) {
            log.error("加载首页数据异常",e);
            e.printStackTrace();
            return Result.failed(e.getMessage());
        }

        log.info("加载首页数据结束");

        return  Result.ok();
    }





}
