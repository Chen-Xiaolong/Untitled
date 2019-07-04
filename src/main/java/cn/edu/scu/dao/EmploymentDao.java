package cn.edu.scu.dao;

import cn.edu.scu.entity.Employment;
import org.apache.ibatis.annotations.Param;
import scala.Int;

import java.util.List;

public interface EmploymentDao {
    int insertOne(@Param("employmentId") int employmentId,
                  @Param("dutyId") int dutyId, @Param("skillId") int skillId);

    List<Employment> queryByEmploymentId(@Param("employmentId") int employmentId);

    List<Employment> queryByDutyId(@Param("dutyId") int dutyId);

    List<Employment> queryBySkillId(@Param("skillId") int skillId);

    List<Integer> queryEmploymentIdByDutyId(@Param("dutyId") int dutyId);

    int getMaxIndex();
}
