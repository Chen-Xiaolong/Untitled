package cn.edu.scu.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class EmploymentDaoTest {

    @Autowired
    EmploymentDao employmentDao;

    @Test
    public void insertOne() {
    }

    @Test
    public void queryByEmploymentId() {
    }

    @Test
    public void queryByDutyId() {
    }

    @Test
    public void queryBySkillId() {
    }

    @Test
    public void getMaxIndex() {
        System.out.println(employmentDao.getMaxIndex());
    }
}