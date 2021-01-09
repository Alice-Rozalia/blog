package com.kuro.controller;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.common.handler.BusinessException;
import com.kuro.model.entity.Link;
import com.kuro.model.entity.Resource;
import com.kuro.service.impl.LinkServiceImpl;
import com.kuro.service.impl.ResourceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "链接资源")
public class LinkController {

    @Autowired
    private ResourceServiceImpl resourceService;

    @Autowired
    private LinkServiceImpl linkService;

    @GetMapping("/pub/resource/list")
    @ApiOperation(value = "查询资源", notes = "分页查询所有的资源链接，不传参数则查询所有")
    public Result findResources(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows
    ) {
        PageResult<Resource> result = this.resourceService.findResources(page, rows);
        return Result.ok().data("resources", result);
    }

    @RequiresPermissions({"resource:add"})
    @PostMapping("/pri/resource/add")
    @ApiOperation(value = "添加资源", notes = "新增资源链接")
    public Result addResource(@RequestBody @Validated Resource resource) {
        if (resource == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.resourceService.addResource(resource);
    }

    @RequiresPermissions({"resource:edit"})
    @PutMapping("/pri/resource/edit")
    @ApiOperation(value = "修改资源", notes = "根据id修改资源链接")
    public Result editResource(@RequestBody @Validated Resource resource) {
        if (resource == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.resourceService.editResource(resource);
    }

    @RequiresPermissions({"resource:delete"})
    @DeleteMapping("/pri/resource/delete")
    @ApiOperation(value = "删除资源", notes = "根据id删除资源链接")
    public Result delResource(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.resourceService.delResource(id);
    }

    @GetMapping("/pub/link/list")
    @ApiOperation(value = "查询友链", notes = "分页查询所有的友情链接，不传参数则查询所有")
    public Result findLink(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows
    ) {
        PageResult<Link> result = this.linkService.findLinks(page, rows);
        return Result.ok().data("links", result);
    }

    @RequiresPermissions({"link:add"})
    @PostMapping("/pri/link/add")
    @ApiOperation(value = "添加友链", notes = "新增友情链接")
    public Result addLink(@RequestBody @Validated Link link) {
        if (link == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.linkService.addLink(link);
    }

    @RequiresPermissions({"link:edit"})
    @PutMapping("/pri/link/edit")
    @ApiOperation(value = "修改友链", notes = "根据id修改友情链接")
    public Result editLink(@RequestBody @Validated Link link) {
        if (link == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.linkService.editLink(link);
    }

    @RequiresPermissions({"link:delete"})
    @DeleteMapping("/pri/link/delete")
    @ApiOperation(value = "删除友链", notes = "根据id删除友情链接")
    public Result delLink(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.linkService.delLink(id);
    }
}
