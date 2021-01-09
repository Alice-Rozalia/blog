package com.kuro.model.vo;

import com.kuro.model.entity.Comment;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CommentVo extends Comment {

    private String nickname;

    private String avatar;

    private List<CommentVo> children;
}
