package com.blogdemo.controller;


import com.blogdemo.domain.ResponseResult;
import com.blogdemo.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 友链(Link)表控制层
 *
 * @author vmlee
 * @since 2022-12-13 11:43:50
 */
@RestController
@RequestMapping("/link")
public class LinkController{
    @Autowired
    private LinkService linkService;
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}

