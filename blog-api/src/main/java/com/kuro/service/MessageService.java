package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Message;
import com.kuro.model.vo.CMCountVo;
import com.kuro.model.vo.MessageVo;

public interface MessageService {

    // 查询留言列表，组装为树形结构后的数据
    PageResult<MessageVo> findMessages(Integer page, Integer rows);

    // 新增留言
    Result addMessage(Message message);

    // 删除留言
    Result deleteMessage(Long id);

    // 查询留言列表（原始数据）
    PageResult<Message> findMessageList(Integer page, Integer rows);

    CMCountVo statistics();
}
