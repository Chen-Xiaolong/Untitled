package cn.edu.scu.service.Impl;

import cn.edu.scu.dao.AdminDao;
import cn.edu.scu.dao.DutyDao;
import cn.edu.scu.dao.EmploymentDao;
import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.User;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.service.AdminService;
import cn.edu.scu.util.CreateMd5;
import cn.edu.scu.util.CreateSalt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private SparkServiceImpl sparkService;

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private DutyDao dutyDao;

    @Autowired
    private CreateSalt createSalt;

    @Autowired
    private CreateMd5 createMd5;

    private String path = AdminServiceImpl.class.getClassLoader().getResource("").getPath();

    @Override
    public UserResult register(String username, String password) {
        if(username.length() < 5||password.equals("")){
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = adminDao.selectCountByUserName(username);
        if (result >= 1) {
            return new UserResult(UserResultEnum.USERNAME_EXIST);
        }
        String salt = createSalt.getSalt();
        String passwordMd5 = createMd5.getMd5ByTwoParameter(password, salt);
        result = adminDao.insertOne(username, passwordMd5, salt);
        if (result != 1) {
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            return new UserResult(UserResultEnum.REGISTER_SUCCESS);
        }
    }

    @Override
    public UserResult login(String username, String password) {
        if(username.equals("")||password.equals("")){
            return new UserResult(UserResultEnum.INSUFFICIENT_PARAMETERS);
        }
        int result = adminDao.selectCountByUserName(username);
        if (result != 1) {
            return new UserResult(UserResultEnum.LOGIN_FAIL);
        } else {
            User user = adminDao.selectByUserName(username);
            if (!user.getUserPasswordHash().equals(
                    createMd5.getMd5ByTwoParameter(password, user.getUserPasswordSalt()))) {
                return new UserResult(UserResultEnum.LOGIN_FAIL);
            } else {
//                user.setUserPasswordHash("");
//                user.setUserPasswordSalt("");
//                String userPasswordHash = user.getUserPasswordHash();
//                return new UserResult(1,
//                        createMd5.getMd5ByTwoParameter(username, userPasswordHash), user);
                return new UserResult(UserResultEnum.LOGIN_SUCCESS, user);
            }
        }
    }

    @Override
    public UserResult addEmployment(String name, String hash, String duty, String[] skills) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }

        File f = new File(path+"data/"+duty+".txt");
        String buf = "";
        try{
            if (!f.exists()) {
                f.createNewFile();// 不存在则创建
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(f,true));
            if(dutyDao.queryCountByDutyName(duty)==0){
                dutyDao.insertOne(duty);
            }
            buf += duty + ",";
            if(skills != null){
                for (String skill : skills){
                    skill = skillTrim(skill);
                    buf += skill + ",";
                    if(skillDao.queryCountBySkillName(skill) == 0){
                        skillDao.insertOne(skill);
                    }
                }
            }
            output.write(buf.substring(0, buf.length()-1) + "\n");
            output.flush();
            output.close();
        } catch (Exception e){
            e.printStackTrace();
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        }

//        int index = employmentDao.getMaxIndex()+1;
//        int dutyId = dutyDao.queryByDutyName(duty).getDutyId();
//        if(skills != null)
//            for (String skill : skills) {
//                skill = skillTrim(skill);
//                Skill skill1 = skillDao.queryBySkillName(skill);
//                if(skill != null){
//                    employmentDao.insertOne(index, dutyId, skill1.getSkillId());
//                }
//
//            }


        return new UserResult(UserResultEnum.OPERATION_SUCCESS);
    }

    @Override
    public UserResult createFPTreeModel(String name, String hash, String duty) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        if(sparkService.createFPTreeModel(duty) == null){
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            return new UserResult(UserResultEnum.OPERATION_SUCCESS);
        }
    }

    private boolean isLogin(String name, String hash) {
        if (name.equals("") || hash.equals(""))
            return false;
        int result = adminDao.selectCountByUserName(name);
        if (result != 1) {
            return false;
        } else {
            User user = adminDao.selectByUserName(name);
            return createMd5.getMd5ByTwoParameter(name, user.getUserPasswordHash()).equals(hash);
        }
    }

    private String skillTrim(String skill){
        if(skill.startsWith("["))
            skill = skill.substring(1);
        if(skill.endsWith("]"))
            skill = skill.substring(0, skill.length()-1);
        return skill.trim();
    }
}
