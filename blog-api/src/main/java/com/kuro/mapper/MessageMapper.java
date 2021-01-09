package com.kuro.mapper;

import com.kuro.model.entity.Message;
import com.kuro.model.vo.MessageVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MessageMapper extends Mapper<Message> {

    List<MessageVo> findMessages(@Param("page") Integer page, @Param("rows") Integer rows);

    Long count();

    void deleteMessageById(@Param("id") Long id);

    Long countTotal();
}
