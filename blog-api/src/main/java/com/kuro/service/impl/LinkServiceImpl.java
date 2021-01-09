package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.LinkMapper;
import com.kuro.model.entity.Link;
import com.kuro.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    /**
     * 分页查询友情链接列表
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<Link> findLinks(Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }

        List<Link> links = this.linkMapper.selectAll();

        PageInfo<Link> pageInfo = new PageInfo<>(links);
        return new PageResult<>(pageInfo.getTotal(), links);
    }

    /**
     * 新增友情链接
     * @param link
     * @return
     */
    @Override
    public Result addLink(Link link) {
        int result = this.linkMapper.insertSelective(link);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        return Result.custom(ResultCode.ADD_SUCCESS);
    }

    @Override
    public Result editLink(Link link) {
        int result = this.linkMapper.updateByPrimaryKeySelective(link);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result delLink(Long id) {
        int result = this.linkMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }
}
