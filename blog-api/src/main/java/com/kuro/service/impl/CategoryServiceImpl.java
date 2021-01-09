package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.ArticleMapper;
import com.kuro.mapper.CategoryMapper;
import com.kuro.model.entity.Article;
import com.kuro.model.entity.Category;
import com.kuro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 分页查询分类
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<Category> findCategories(Integer page, Integer rows, Long id) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        // 添加分页
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        if (id != null) {
            criteria.andEqualTo("id", id);
        }
        List<Category> categories = this.categoryMapper.selectByExample(example);

        PageInfo<Category> pageInfo = new PageInfo<>(categories);
        return new PageResult<>(pageInfo.getTotal(), categories);
    }

    /**
     * 添加分类
     * @param name
     * @return
     */
    @Override
    public Result addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        // 查询该分类是否已经存在
        int exist = this.categoryMapper.selectCount(category);
        if (exist != 0) {
            return Result.custom(ResultCode.CATEGORY_EXIST);
        }
        category.setCreateTime(System.currentTimeMillis());
        int result = this.categoryMapper.insertSelective(category);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        return Result.custom(ResultCode.ADD_SUCCESS);
    }

    @Override
    public Result editCategory(Category category) {
        int result = this.categoryMapper.updateByPrimaryKeySelective(category);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result delCategory(Long id) {
        // 先查询文章列表中是否有文章使用了该分类，如果使用了，删除失败！
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("cid", id);
        List<Article> articles = this.articleMapper.selectByExample(example);
        if (!articles.isEmpty()) {
            return Result.custom(ResultCode.CATEGORY_DELETE_ERROR_BECAUSE_USE);
        }
        int result = this.categoryMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }
}
