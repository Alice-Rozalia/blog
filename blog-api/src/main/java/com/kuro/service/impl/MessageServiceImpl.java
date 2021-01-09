package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.ActiveUser;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.CommentMapper;
import com.kuro.mapper.MessageMapper;
import com.kuro.model.entity.Message;
import com.kuro.model.vo.CMCountVo;
import com.kuro.model.vo.MessageVo;
import com.kuro.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageResult<MessageVo> findMessages(Integer page, Integer rows) {
        List<MessageVo> messages = this.messageMapper.findMessages((page - 1) * rows, rows);
        return new PageResult<>(this.messageMapper.count(), messages);
    }

    @Override
    public Result addMessage(Message message) {
        // 获取当前用户
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(message.getContent())) {
            return Result.custom(ResultCode.MESSAGE_IS_BLANK);
        }
        message.setUserId(activeUser.getUser().getId());
        message.setTime(System.currentTimeMillis());
        int result = this.messageMapper.insertSelective(message);
        if (result == 0) {
            return Result.custom(ResultCode.MESSAGE_ERROR);
        }
        return Result.custom(ResultCode.MESSAGE_SUCCESS);
    }

    @Override
    public Result deleteMessage(Long id) {
        int i = this.messageMapper.deleteByPrimaryKey(id);
        if (i == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }

    @Override
    public PageResult<Message> findMessageList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Message> messages = this.messageMapper.selectAll();
        PageInfo<Message> pageInfo = new PageInfo<>(messages);
        return new PageResult<>(pageInfo.getTotal(), messages);
    }

    @Override
    public CMCountVo statistics() {
        CMCountVo countVo = new CMCountVo();
        countVo.setCommentCount(this.commentMapper.countTotal());
        countVo.setMessageCount(this.messageMapper.countTotal());
        return countVo;
    }

}
