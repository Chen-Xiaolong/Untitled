package cn.edu.scu.dao;

import cn.edu.scu.entity.Duty;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DutyDao {
    int insertOne(@Param("dutyName") String dutyName, @Param("description") String description);

    Duty queryByDutyName(@Param("dutyName") String dutyName);

    Duty queryByDutyId(@Param("dutyId") int dutyId);

    int queryCountByDutyName(@Param("dutyName") String dutyName);

    List<Duty> queryAll();
}
