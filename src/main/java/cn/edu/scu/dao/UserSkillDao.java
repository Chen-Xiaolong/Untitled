package cn.edu.scu.dao;

import cn.edu.scu.entity.UserSkill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserSkillDao {
    int insertOne(@Param("userId")int userId, @Param("skillId")int skillId);

    List<UserSkill> queryByUserId(@Param("skillId")int skillId);
}
