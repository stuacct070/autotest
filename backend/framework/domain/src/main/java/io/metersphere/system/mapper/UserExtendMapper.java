package io.metersphere.system.mapper;

import io.metersphere.system.domain.UserExtend;
import io.metersphere.system.domain.UserExtendExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserExtendMapper {
    long countByExample(UserExtendExample example);

    int deleteByExample(UserExtendExample example);

    int deleteByPrimaryKey(String userId);

    int insert(UserExtend record);

    int insertSelective(UserExtend record);

    List<UserExtend> selectByExampleWithBLOBs(UserExtendExample example);

    List<UserExtend> selectByExample(UserExtendExample example);

    UserExtend selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") UserExtend record, @Param("example") UserExtendExample example);

    int updateByExampleWithBLOBs(@Param("record") UserExtend record, @Param("example") UserExtendExample example);

    int updateByExample(@Param("record") UserExtend record, @Param("example") UserExtendExample example);

    int updateByPrimaryKeySelective(UserExtend record);

    int updateByPrimaryKeyWithBLOBs(UserExtend record);

    int updateByPrimaryKey(UserExtend record);
}