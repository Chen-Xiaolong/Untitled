package cn.edu.scu.dao;

import cn.edu.scu.entity.Employment;
import org.apache.ibatis.annotations.Param;

public interface EmploymentDao {
    int insertOne(@Param("employmentId") int employmentId,
                  @Param("dutyId") int dutyId, @Param("skillId") int skillId);

    Employment queryByEmploymentId(@Param("employmentId") int employmentId);

    Employment queryByDutyId(@Param("dutyId") int dutyId);

    Employment queryBySkillId(@Param("skillId") int skillId);

    int getMaxIndex();
}
