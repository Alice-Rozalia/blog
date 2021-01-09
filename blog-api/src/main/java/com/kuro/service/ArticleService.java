package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Article;
import com.kuro.model.vo.ArticleVo;

import java.util.List;

public interface ArticleService {
    /**
     * 查询文章列表
     * @param key       搜索关键字
     * @param page      当前页
     * @param rows      每页显示条数
     * @return
     */
    PageResult<ArticleVo> findArticles(String key, Integer page, Integer rows);

    // 新增文章
    Result addArticle(Article article, String tags);

    // 修改文章
    Result editArticle(Article article, String tags);

    // 删除文章
    Result deleteArticle(Long id);

    // 根据id查询文章
    ArticleVo findArticleById(Long id);

    // 查询阅读量前6的文章
    List<Article> ranking();
}
