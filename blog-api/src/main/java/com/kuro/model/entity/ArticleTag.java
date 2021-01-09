package com.kuro.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "tb_article_tag")
@ApiModel(value="ArticleTag对象", description="文章标签中间表")
public class ArticleTag {

    private Long aid;

    private Long tid;
}
