package cn.edu.scu.dao;

import cn.edu.scu.entity.User;
import org.apache.ibatis.annotations.Param;

public interface AdminDao {
    int insertOne(@Param("userName") String userName,
                  @Param("userPasswordHash") String userPasswordHash,
                  @Param("userPasswordSalt") String userPasswordSalt);

    User selectByUserName(@Param("userName") String userName);

    User selectByUserId(@Param("userId") long userId);

    int selectCountByUserName(@Param("userName") String userName);
}
