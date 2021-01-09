package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.kuro.common.entity.ActiveUser;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.*;
import com.kuro.model.entity.Article;
import com.kuro.model.entity.ArticleTag;
import com.kuro.model.vo.ArticleVo;
import com.kuro.service.AliOssService;
import com.kuro.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AliOssService aliOssService;

    /**
     * 获取文章列表
     * @param key       搜索关键字
     * @param page      当前页
     * @param rows      每页显示条数
     * @return
     */
    @Override
    public PageResult<ArticleVo> findArticles(String key, Integer page, Integer rows) {
        List<ArticleVo> articles;
        Long total;
        if (StringUtils.isNoneBlank(key)) {
            articles = this.articleMapper.findArticleList(key, (page - 1) * rows, rows);
            total = this.articleMapper.count(key);
        } else {
            articles = this.articleMapper.findArticleList(null, (page - 1) * rows, rows);
            total = this.articleMapper.count(null);
        }

        if (articles != null && articles.size() != 0) {
            articles.forEach(articleVo -> {
                Integer count = this.commentMapper.findCountByArticleId(articleVo.getId());
                articleVo.setCommentCount(count);
            });
        }
        return new PageResult<>(total, articles);
    }

    /**
     * 新增文章
     * @param article
     * @param tags
     * @return
     */
    @Override
    @Transactional
    public Result addArticle(Article article, String tags) {
        // 根据文章标题判断该文章是否已经存在
        Article check = new Article();
        check.setTitle(article.getTitle());
        int count = this.articleMapper.selectCount(check);
        if (count != 0) {
            return Result.custom(ResultCode.ARTICLE_ALREADY_EXIST);
        }
        article.setCreateTime(System.currentTimeMillis());
        // 获取当前登录用户的信息
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        article.setUid(activeUser.getUser().getId());
        int result = this.articleMapper.insertSelective(article);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        // 根据文章标题查找文章
        Article find = this.articleMapper.selectOne(check);
        // 记录tb_article_tag表上文章对应的tag
        ArticleTag articleTag = new ArticleTag();
        String[] split = tags.split(",");
        for (String s : split) {
            articleTag.setAid(find.getId());
            articleTag.setTid(Long.parseLong(s));
            int i = this.articleTagMapper.insertSelective(articleTag);
            if (i == 0) {
                this.articleMapper.deleteByPrimaryKey(find.getId());
                return Result.custom(ResultCode.ADD_ERROR);
            }
        }
        return Result.custom(ResultCode.ADD_SUCCESS);
    }

    @Override
    @Transactional
    public Result editArticle(Article article, String tags) {
        // 先删除article_tag中间表的记录
        this.articleTagMapper.deleteArticleTag(article.getId());
        String[] split = tags.split(",");
        ArticleTag articleTag = new ArticleTag();
        for (String s : split) {
            articleTag.setAid(article.getId());
            articleTag.setTid(Long.parseLong(s));
            this.articleTagMapper.insertSelective(articleTag);
        }
        int result = this.articleMapper.updateByPrimaryKeySelective(article);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    @Transactional
    public Result deleteArticle(Long id) {
        // 删除article_tag中间表的记录
        this.articleTagMapper.deleteArticleTag(id);
        // 删除跟文章相关的评论
        this.commentMapper.deleteCommentByArticleId(id);
        // 删除图片
        Article article = this.articleMapper.selectByPrimaryKey(id);
        String[] split = article.getCover().split(".com/");
        aliOssService.deleteFile(split[1]);
        // 最后再删除文章
        int result = this.articleMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }

    @Override
    public ArticleVo findArticleById(Long id) {
        return this.articleMapper.findDetailById(id);
    }

    @Override
    public List<Article> ranking() {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("views desc,id asc");
        PageHelper.startPage(1, 6);
        List<Article> articles = this.articleMapper.selectByExample(example);
        return articles;
    }

}
