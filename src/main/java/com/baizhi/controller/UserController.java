package com.baizhi.controller;

import com.baizhi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("showUserByTime")
    public Map showUserByTime(){
        Map map = userService.showUserByTime();
        return map;
    }
    @RequestMapping("showUserByAddress")
    public Map showUserByAddress(){
        Map map = userService.showUserByAddress();
        return map;
    }
}
