package com.kuro.mapper;

import com.kuro.model.entity.ArticleTag;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ArticleTagMapper extends Mapper<ArticleTag> {

    void deleteArticleTag(@Param("aid") Long aid);

}
