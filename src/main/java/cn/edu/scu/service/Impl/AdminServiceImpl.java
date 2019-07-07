package cn.edu.scu.service.Impl;

import cn.edu.scu.dao.AdminDao;
import cn.edu.scu.dao.DutyDao;
import cn.edu.scu.dao.EmploymentDao;
import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.Skill;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        File f1 = new File(path+"data/"+duty+".txt");
        File f2 = new File(path+"data/final_data.txt");
        String buf1 = "";
        String buf2 = "";
        try{
            if (!f1.exists()) {
                f1.createNewFile();// 不存在则创建
            }
            BufferedWriter output1 = new BufferedWriter(new FileWriter(f1,true));
            if(dutyDao.queryCountByDutyName(duty.trim())==0){
                return new UserResult(UserResultEnum.UNKNOWN_ERROR);
            }
            buf1 += duty + ",";
            if(skills != null){
                for (String skill : skills){
                    skill = skillTrim(skill);
                    if(skillDao.queryCountBySkillName(skill) == 0){
//                        skillDao.insertOne(skill);
                        continue;
                    }
                    buf1 += skill + ",";
                }
                output1.write(buf1.substring(0, buf1.length()-1) + "\n");
            }
            output1.flush();
            output1.close();

            BufferedWriter output2 = new BufferedWriter(new FileWriter(f2,true));
            int dutyId = dutyDao.queryByDutyName(duty).getDutyId();
            buf2 += dutyId-1 + " ";
            if(skills != null){
                List<Skill> skillList = new ArrayList<>();
                for (String skill : skills){
                    skill = skillTrim(skill);
                    if(skillDao.queryCountBySkillName(skill) == 0){
//                        skillDao.insertOne(skill);
                        continue;
                    }
//                    int skillId = skillDao.queryBySkillName(skill).getSkillId();
//                    buf2 += skillId + ":1 ";

                    skillList.add(skillDao.queryBySkillName(skill));

                }
                skillList.sort(Comparator.comparingInt(Skill::getSkillId));
                for (Skill skill : skillList) {
                    buf2 += skill.getSkillId() + ":1 ";
                }

                output2.write(buf2.substring(0, buf2.length()-1) + "\n");
            }
            output2.flush();
            output2.close();
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

    @Override
    public UserResult createRecommendModel(String name, String hash){
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        if(sparkService.createNaiveBayesModel() == null){
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        } else {
            return new UserResult(UserResultEnum.OPERATION_SUCCESS);
        }
    }

    @Override
    public UserResult addDuty(String name, String hash, String duty, String description) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        int result = dutyDao.insertOne(skillTrim(duty), description);
        if(result == 1){
            return new UserResult(UserResultEnum.OPERATION_SUCCESS);
        } else {
            return new UserResult(UserResultEnum.UNKNOWN_ERROR);
        }
    }

    @Override
    public UserResult addSkill(String name, String hash, String[] skill) {
        if(!isLogin(name, hash)){
            return new UserResult(UserResultEnum.UNLOGINED);
        }
        for (String s : skill) {
            skillDao.insertOne(skillTrim(s));
        }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS);
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
