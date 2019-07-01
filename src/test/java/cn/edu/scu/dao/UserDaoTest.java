package cn.edu.scu.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void insertOne() {
        String name = "helloworld";
        String phone = "12345678910";
        String password = "123456";
        String salt = "123";
        Integer result = userDao.insertOne(name, password, salt);
        System.out.println(result.toString());
    }

    @Test
    public void selectByUserName() {
    }

    @Test
    public void selectByUserId() {
    }

    @Test
    public void selectCountByUserName() {
    }
}