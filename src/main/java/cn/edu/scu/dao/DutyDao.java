package cn.edu.scu.dao;

import cn.edu.scu.entity.Duty;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DutyDao {
    int insertOne(@Param("dutyName") String dutyName);

    Duty queryByDutyName(@Param("dutyName") String dutyName);

    Duty queryByDutyId(@Param("dutyId") int dutyId);

    List<Duty> queryAll();
}
