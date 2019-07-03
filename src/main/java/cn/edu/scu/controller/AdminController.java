package cn.edu.scu.controller;

import cn.edu.scu.dto.UserResult;
import cn.edu.scu.service.AdminService;
import cn.edu.scu.util.CreateMd5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CreateMd5 createMd5;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public UserResult login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserResult userResult = adminService.login(username, password);
        if (userResult.getStatus() == 1) {
            String hash = createMd5.getMd5ByTwoParameter(userResult.getUser().getUserName(), userResult.getUser().getUserPasswordHash());
            Cookie cookie = new Cookie("username",username);
            cookie.setPath("/admin");
            response.addCookie(cookie);
            cookie = new Cookie("hash",hash);
            cookie.setPath("/admin");
            response.addCookie(cookie);
        }
        return userResult;
    }

    @RequestMapping(value = "/addEmployment", method = RequestMethod.POST)
    @ResponseBody
    public UserResult addEmployment(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        String duty = request.getParameter("duty");
        String[] skill = request.getParameter("skill").split(",");
        return adminService.addEmployment(username, hash, duty, skill);

    }
}
