package cn.edu.scu.dao;

import cn.edu.scu.entity.Skill;
import org.apache.ibatis.annotations.Param;

public interface SkillDao {
    int insertOne(@Param("skillName") String skillName);

    Skill queryBySkillName(@Param("skillName") String skillName);

    Skill queryBySkillId(@Param("skillId") int skillId);
}
