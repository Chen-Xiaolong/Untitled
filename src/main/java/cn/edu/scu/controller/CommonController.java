package cn.edu.scu.controller;

import cn.edu.scu.dto.UserResult;
import cn.edu.scu.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/common")
public class CommonController {
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/queryAllSkill", method = RequestMethod.POST)
    @ResponseBody
    public UserResult queryAllSkill(HttpServletRequest request, HttpServletResponse response) {
        return commonService.queryAllSkill();
    }

    @RequestMapping(value = "/queryAllDuty", method = RequestMethod.POST)
    @ResponseBody
    public UserResult queryAllDuty(HttpServletRequest request, HttpServletResponse response) {
        return commonService.queryAllDuty();
    }

}
