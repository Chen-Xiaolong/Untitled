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
            userResult.getUser().setUserPasswordHash(null);
            userResult.getUser().setUserPasswordSalt(null);
            Cookie cookie = new Cookie("username",username);
            cookie.setPath("/Untitled/admin/");
            response.addCookie(cookie);
            cookie = new Cookie("hash",hash);
            cookie.setPath("/Untitled/admin/");
            response.addCookie(cookie);
        }
        return userResult;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("username") || cookie.getName().equals("hash")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    @RequestMapping(value = "/addEmployment", method = RequestMethod.POST)
    @ResponseBody
    public UserResult addEmployment(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return adminService.addEmployment(username, hash, "", null);
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        String duty = request.getParameter("duty");
        String all = request.getParameter("skill");
        String[] skill = null;
        if(all.length() > 0)
            skill = all.split(",");
        return adminService.addEmployment(username, hash, duty, skill);
    }

    @RequestMapping(value = "/analyseModeling", method = RequestMethod.POST)
    @ResponseBody
    public UserResult analyseModeling(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return adminService.createFPTreeModel(username, hash, "");
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        String duty = request.getParameter("duty");
        return adminService.createFPTreeModel(username, hash, duty);
    }

    @RequestMapping(value = "/recommendModeling", method = RequestMethod.POST)
    @ResponseBody
    public UserResult recommendModeling(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            return adminService.createRecommendModel(username, hash);
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        return adminService.createRecommendModel(username, hash);
    }

    @RequestMapping(value = "/addSkill", method = RequestMethod.POST)
    @ResponseBody
    public UserResult addSkill(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return adminService.createFPTreeModel(username, hash, "");
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        String all = request.getParameter("skill");
        String[] skill = null;
        if(all.length() > 0)
            skill = all.split(",");
        return adminService.addSkill(username, hash, skill);
    }

    @RequestMapping(value = "/addDuty", method = RequestMethod.POST)
    @ResponseBody
    public UserResult addDuty(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return adminService.createFPTreeModel(username, hash, "");
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        String duty = request.getParameter("duty");
        String description = request.getParameter("description");

        return adminService.addDuty(username, hash, duty, description);
    }

}
