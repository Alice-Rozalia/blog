package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.ArticleTagMapper;
import com.kuro.mapper.TagMapper;
import com.kuro.model.entity.ArticleTag;
import com.kuro.model.entity.Tag;
import com.kuro.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageResult<Tag> findTags(Integer page, Integer rows) {
        // 添加分页
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        List<Tag> tags = this.tagMapper.selectAll();

        PageInfo<Tag> pageInfo = new PageInfo<>(tags);
        return new PageResult<>(pageInfo.getTotal(), tags);
    }

    /**
     * 添加Tag标签
     * @param tag
     * @return
     */
    @Override
    public Result addTag(Tag tag) {
        Tag chechTag = new Tag();
        chechTag.setName(tag.getName());
        // 查询该tag标签是否已经存在
        int exist = this.tagMapper.selectCount(chechTag);
        if (exist != 0) {
            return Result.custom(ResultCode.TAG_EXIST);
        }
        tag.setCreateTime(System.currentTimeMillis());
        int result = this.tagMapper.insertSelective(tag);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        return Result.custom(ResultCode.ADD_SUCCESS);
    }

    @Override
    public Result editTag(Tag tag) {
        int result = this.tagMapper.updateByPrimaryKeySelective(tag);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result delTag(Long id) {
        // 查询该tag标签是否已经被使用
        Example example = new Example(ArticleTag.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("tid", id);
        List<ArticleTag> articleTags = this.articleTagMapper.selectByExample(example);
        if (!articleTags.isEmpty()) {
            return Result.custom(ResultCode.TAG_DELETE_ERROR_BECAUSE_USE);
        }
        int result = this.tagMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }

}
