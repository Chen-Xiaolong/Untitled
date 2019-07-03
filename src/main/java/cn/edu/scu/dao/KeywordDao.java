package cn.edu.scu.dao;

import cn.edu.scu.entity.Keyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KeywordDao {
    int insertOne(@Param("keywordName") String keywordName);

    Keyword queryByKeywordName(@Param("keywordName") String keywordName);

    Keyword queryByKeywordId(@Param("keywordId") int keywordId);

    List<Keyword> queryAll();
}
