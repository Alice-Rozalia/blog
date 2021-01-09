package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Link;

public interface LinkService {

    /**
     * 分页查询友情链接列表
     * @param page
     * @param rows
     * @return
     */
    PageResult<Link> findLinks(Integer page, Integer rows);

    /**
     * 新增友情链接
     * @param link
     * @return
     */
    Result addLink(Link link);

    // 修改友情链接
    Result editLink(Link link);

    // 删除友情链接
    Result delLink(Long id);
}
