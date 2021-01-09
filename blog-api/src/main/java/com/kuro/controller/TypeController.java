package com.kuro.controller;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.common.handler.BusinessException;
import com.kuro.model.entity.Category;
import com.kuro.model.entity.Tag;
import com.kuro.service.impl.CategoryServiceImpl;
import com.kuro.service.impl.TagServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "标签分类")
public class TypeController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private TagServiceImpl tagService;

    @GetMapping("/pub/category/list")
    @ApiOperation(value = "查询分类", notes = "分页查询所有的分类，不传参数则查询所有")
    public Result findCategories(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "id", required = false) Long id
    ) {
        PageResult<Category> result = this.categoryService.findCategories(page, rows, id);
        return Result.ok().data("categories", result);
    }

    @RequiresPermissions({"category:add"})
    @PostMapping("/pri/category/add")
    @ApiOperation(value = "添加分类", notes = "新增分类")
    public Result addCategory(@RequestBody @Validated Category category) {
        if (StringUtils.isBlank(category.getName())) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.categoryService.addCategory(category.getName());
    }

    @RequiresPermissions({"category:edit"})
    @PutMapping("/pri/category/edit")
    @ApiOperation(value = "修改分类", notes = "根据id修改分类")
    public Result editCategory(@RequestBody @Validated Category category) {
        if (category == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.categoryService.editCategory(category);
    }

    @RequiresPermissions({"category:delete"})
    @DeleteMapping("/pri/category/delete")
    @ApiOperation(value = "删除分类", notes = "根据id删除未被使用的分类")
    public Result delCategory(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.categoryService.delCategory(id);
    }

    @GetMapping("/pub/tag/list")
    @ApiOperation(value = "查询标签", notes = "分页查询所有的Tag标签，不传参数则查询所有")
    public Result findTags(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows
    ) {
        PageResult<Tag> result = this.tagService.findTags(page, rows);
        return Result.ok().data("tags", result);
    }

    @RequiresPermissions({"tag:add"})
    @PostMapping("/pri/tag/add")
    @ApiOperation(value = "添加标签", notes = "新增Tag标签")
    public Result addTag(@RequestBody @Validated Tag tag) {
        if (tag == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.tagService.addTag(tag);
    }

    @RequiresPermissions({"tag:edit"})
    @PutMapping("/pri/tag/edit")
    @ApiOperation(value = "修改标签", notes = "根据id修改标签")
    public Result editTag(@RequestBody @Validated Tag tag) {
        if (tag == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.tagService.editTag(tag);
    }

    @RequiresPermissions({"tag:delete"})
    @DeleteMapping("/pri/tag/delete")
    @ApiOperation(value = "删除标签", notes = "根据id删除未被使用的标签")
    public Result delTag(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.tagService.delTag(id);
    }
}
