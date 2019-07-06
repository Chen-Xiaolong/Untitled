package cn.edu.scu.controller;

import cn.edu.scu.dto.UserResult;
import cn.edu.scu.service.Impl.SparkServiceImpl;
import cn.edu.scu.service.UserService;
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
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CreateMd5 createMd5;
    @Autowired
    private SparkServiceImpl sparkService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public UserResult register(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return userService.register(username, password);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public UserResult login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserResult userResult = userService.login(username, password);
        if (userResult.getStatus() == 1) {
            String hash = createMd5.getMd5ByTwoParameter(userResult.getUser().getUserName(), userResult.getUser().getUserPasswordHash());
            userResult.getUser().setUserPasswordHash(null);
            userResult.getUser().setUserPasswordSalt(null);
            Cookie cookie = new Cookie("username",username);
            cookie.setPath("/Untitled/user/");
            response.addCookie(cookie);
            cookie = new Cookie("hash",hash);
            cookie.setPath("/Untitled/user/");
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

    @RequestMapping(value = "/querySkill", method = RequestMethod.POST)
    @ResponseBody
    public UserResult querySkill(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return userService.querySkill(username, hash);
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        return userService.querySkill(username, hash);
    }

    @RequestMapping(value = "/addSkill", method = RequestMethod.POST)
    @ResponseBody
    public UserResult addSkill(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return userService.addSkill(username, hash, null);
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            } else {
                System.out.println("Cookie Name: "+cookie.getName());
                System.out.println("Cookie Value: "+cookie.getValue());
            }
        }
        String all = request.getParameter("skill");
        String[] skill = null;
        if(all.length() > 0)
            skill = all.split(",");
        return userService.addSkill(username, hash, skill);
    }

    @RequestMapping(value = "/deleteSkill", method = RequestMethod.POST)
    @ResponseBody
    public UserResult deleteSkill(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return userService.deleteSkill(username, hash, "");
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            } else {
                System.out.println("Cookie Name: "+cookie.getName());
                System.out.println("Cookie Value: "+cookie.getValue());
            }
        }
        String skill = request.getParameter("skill");
        return userService.deleteSkill(username, hash, skill);
    }

    @RequestMapping(value = "/jobAnalyse", method = RequestMethod.POST)
    @ResponseBody
    public UserResult jobAnalyse(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            System.out.println(request.getHeader("cookie"));
            return sparkService.analyse(username, hash, "");
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        String duty = request.getParameter("duty");
        return sparkService.analyse(username, hash, duty);
    }

    @RequestMapping(value = "/jobRecommend", method = RequestMethod.POST)
    @ResponseBody
    public UserResult jobRecommend(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String username = "";
        String hash = "";
        if(cookies == null || cookies.length == 0){
            return sparkService.recommend(username, hash);
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("username")){
                username = cookie.getValue();
            } else if (cookie.getName().equals("hash")){
                hash = cookie.getValue();
            }
        }
        return sparkService.recommend(username, hash);
    }
}
