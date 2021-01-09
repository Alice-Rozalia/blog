package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.ActiveUser;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.CommentMapper;
import com.kuro.model.entity.Comment;
import com.kuro.model.vo.CommentVo;
import com.kuro.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageResult<Comment> findComments(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Comment> comments = this.commentMapper.selectAll();
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        return new PageResult<>(pageInfo.getTotal(), comments);
    }

    @Override
    public PageResult<CommentVo> findCommentsByArticleId(Integer page, Integer rows, Long aid) {
        List<CommentVo> comments = this.commentMapper.findCommentsByArticleId((page - 1) * rows, rows, aid);
        return new PageResult<>(this.commentMapper.count(aid), comments);
    }

    @Override
    public Result addComment(Comment comment) {
        // 获取当前用户
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isBlank(comment.getContent())) {
            return Result.custom(ResultCode.COMMENT_IS_BLANK);
        }
        comment.setUid(activeUser.getUser().getId());
        comment.setCommentTime(System.currentTimeMillis());
        int result = this.commentMapper.insertSelective(comment);
        if (result == 0) {
            return Result.custom(ResultCode.COMMENT_ERROR);
        }
        return Result.custom(ResultCode.COMMENT_SUCCESS);
    }

    @Override
    public Result editComment(Comment comment) {
        return null;
    }

    @Override
    public Result deleteComment(Long id) {
        int result = this.commentMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }
}
