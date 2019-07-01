package cn.edu.scu.controller;


import cn.edu.scu.service.SparkTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private SparkTest sparkTest;

    @RequestMapping(value = "/spark", method = RequestMethod.GET)
    @ResponseBody
    public String register(HttpServletRequest request, HttpServletResponse response){
        return sparkTest.test();
    }
}
