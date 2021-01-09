package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Comment;
import com.kuro.model.vo.CommentVo;

public interface CommentService {

    // 查询评论列表
    PageResult<Comment> findComments(Integer page, Integer rows);

    // 根据文章id查询评论集合（树形结构）
    PageResult<CommentVo> findCommentsByArticleId(Integer page, Integer rows, Long aid);

    // 新增评论
    Result addComment(Comment comment);

    // 修改评论
    Result editComment(Comment comment);

    // 删除评论
    Result deleteComment(Long id);
}
