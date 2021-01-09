package com.kuro.model.vo;

import com.kuro.model.entity.Article;
import com.kuro.model.entity.Tag;
import com.kuro.model.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ArticleVo extends Article {

    private String categoryName;

    private User author;

    private List<Tag> tags;

    private Integer commentCount;
}
