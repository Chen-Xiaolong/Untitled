package cn.edu.scu.service;

import cn.edu.scu.dto.UserResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring/spring-dao.xml", "classpath:/spring/spring-service.xml"})
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void register() {
        String username = "czhssb";
        String password = "czhssb";
        UserResult result = adminService.register(username, password);
        System.out.println(result);
    }

    @Test
    public void login() {
        String username = "wdndm";
        String password = "asdasd";
        UserResult result = adminService.login(username, password);
        System.out.println(result);
    }
}