package com.kuro.mapper;

import com.kuro.model.entity.Article;
import com.kuro.model.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ArticleMapper extends Mapper<Article> {

    List<ArticleVo> findArticleList(@Param("key") String key, @Param("page") Integer page, @Param("rows") Integer rows);

    Long count(@Param("key") String key);

    ArticleVo findDetailById(@Param("id") Long ArticleId);

}
