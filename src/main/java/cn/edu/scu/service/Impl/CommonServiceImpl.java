package cn.edu.scu.service.Impl;

import cn.edu.scu.dao.DutyDao;
import cn.edu.scu.dao.SkillDao;
import cn.edu.scu.dto.UserResult;
import cn.edu.scu.entity.Duty;
import cn.edu.scu.entity.Skill;
import cn.edu.scu.enums.UserResultEnum;
import cn.edu.scu.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private DutyDao dutyDao;

    @Override
    public UserResult queryAllSkill() {
        List<String> skillArr = new ArrayList<>();
        List<Skill> skills = skillDao.queryAll();
        for (Skill skill : skills) {
            skillArr.add(skill.getSkillName());
        }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS, Arrays.toString(skillArr.toArray()));
    }

    @Override
    public UserResult queryAllDuty() {
        List<String> dutyArr = new ArrayList<>();
        List<Duty> duties = dutyDao.queryAll();
        for (Duty duty : duties) {
            dutyArr.add(duty.getDutyName());
        }
        return new UserResult(UserResultEnum.OPERATION_SUCCESS, Arrays.toString(dutyArr.toArray()));
    }
}
