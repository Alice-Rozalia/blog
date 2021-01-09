package com.kuro.mapper;

import com.kuro.model.entity.Comment;
import com.kuro.model.vo.CommentVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CommentMapper extends Mapper<Comment> {

    void deleteCommentByArticleId(@Param("aid") Long aid);

    Integer findCountByArticleId(@Param("aid") Long aid);

    List<CommentVo> findCommentsByArticleId(@Param("page") Integer page, @Param("rows") Integer rows, @Param("aid") Long aid);

    Long count(@Param("aid") Long aid);

    Long countTotal();
}
