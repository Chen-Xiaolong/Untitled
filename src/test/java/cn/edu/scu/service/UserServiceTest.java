package cn.edu.scu.service;

import cn.edu.scu.dto.UserResult;
import cn.edu.scu.service.Impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring/spring-dao.xml", "classpath:/spring/spring-service.xml"})
public class UserServiceTest {

    @Autowired
    UserServiceImpl userService;

    @Test
    public void register() {
        String username = "wdndm";
        String password = "asdasd";
        UserResult result = userService.register(username, password);
        System.out.println(result);
    }

    @Test
    public void login() {
        String username = "wdndm";
        String password = "asdasd";
        UserResult result = userService.login(username, password);
        System.out.println(result);
    }
}