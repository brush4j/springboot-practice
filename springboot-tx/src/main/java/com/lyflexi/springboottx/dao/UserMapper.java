package com.lyflexi.springboottx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyflexi.springboottx.model.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
  * Created with IntelliJ IDEA.
  * @author： hmly
  * @date： 2025/7/24
  * @description： 
  * @modifiedBy：
  * @version: 1.0
*/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserPo> {
}
