package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Category;

public interface CategoryService {
    /**
     * 分页查询分类
     * @param page
     * @param rows
     * @return
     */
    PageResult<Category> findCategories(Integer page, Integer rows, Long id);

    /**
     * 添加分类
     * @param name
     * @return
     */
    Result addCategory(String name);

    // 修改分类
    Result editCategory(Category category);

    // 删除分类
    Result delCategory(Long id);
}
