package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Resource;

public interface ResourceService {
    /**
     * 分页查询资源链接
     * @param page
     * @param rows
     * @return
     */
    PageResult<Resource> findResources(Integer page, Integer rows);

    /**
     * 添加资源链接
     * @param resource
     * @return
     */
    Result addResource(Resource resource);

    // 修改资源链接
    Result editResource(Resource resource);

    // 删除资源链接
    Result delResource(Long id);
}
