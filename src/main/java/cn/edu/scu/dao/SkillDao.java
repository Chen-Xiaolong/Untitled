package cn.edu.scu.dao;

import cn.edu.scu.entity.Skill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkillDao {
    int insertOne(@Param("skillName") String skillName);

    Skill queryBySkillName(@Param("skillName") String skillName);

    Skill queryBySkillId(@Param("skillId") int skillId);

    List<Skill> queryAll();
}
