package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.ResourceMapper;
import com.kuro.model.entity.Resource;
import com.kuro.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 分页查询资源链接
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<Resource> findResources(Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        List<Resource> resources = this.resourceMapper.selectAll();

        PageInfo<Resource> pageInfo = new PageInfo<>(resources);
        return new PageResult<>(pageInfo.getTotal(), resources);
    }

    /**
     * 添加资源链接
     * @param resource
     * @return
     */
    @Override
    public Result addResource(Resource resource) {
        resource.setUserId(10086L);
        int result = this.resourceMapper.insertSelective(resource);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        return Result.custom(ResultCode.ADD_SUCCESS);
    }

    @Override
    public Result editResource(Resource resource) {
        int result = this.resourceMapper.updateByPrimaryKeySelective(resource);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result delResource(Long id) {
        int result = this.resourceMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }
}
