package com.kuro.controller;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.common.handler.BusinessException;
import com.kuro.model.entity.Article;
import com.kuro.model.vo.ArticleVo;
import com.kuro.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "文章管理")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/pub/article/list")
    @ApiOperation(value = "查询文章", notes = "分页查询所有的文章列表，不传参数则查询所有")
    public Result findArticle(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<ArticleVo> articles = this.articleService.findArticles(key, page, rows);
        return Result.ok().data("article", articles);
    }

    @RequiresPermissions({"article:add"})
    @PostMapping("/pri/article/add")
    @ApiOperation(value = "发表文章", notes = "发表文章")
    public Result addArticle(@RequestBody @Validated Article article, @RequestParam String tags) {
        if (article == null || StringUtils.isBlank(tags)) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.articleService.addArticle(article, tags);
    }

    @RequiresPermissions({"article:edit"})
    @PutMapping("/pri/article/edit")
    @ApiOperation(value = "修改文章", notes = "根据id修改文章")
    public Result editArticle(@RequestBody @Validated Article article, @RequestParam String tags) {
        if (article == null || StringUtils.isBlank(tags)) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.articleService.editArticle(article, tags);
    }

    @RequiresPermissions({"article:delete"})
    @DeleteMapping("/pri/article/delete")
    @ApiOperation(value = "删除文章", notes = "根据id删除文章")
    public Result deleteArticle(@RequestParam Long id) {
        return this.articleService.deleteArticle(id);
    }

    @GetMapping("/pub/article/find_detail")
    @ApiOperation(value = "查找文章", notes = "根据id查找文章")
    public Result findArticleById(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        Article result = this.articleService.findArticleById(id);
        if (result == null) {
            return Result.custom(ResultCode.ARTICLE_ALREADY_NOT_EXIST);
        }
        return Result.ok().data("article", result);
    }

    @GetMapping("/pub/article/hot")
    @ApiOperation(value = "热门文章", notes = "获取阅读量前6的文章")
    public Result ranking() {
        List<Article> articles = this.articleService.ranking();
        return Result.ok().data("hot_article", articles);
    }
}
