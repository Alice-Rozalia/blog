package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Tag;

public interface TagService {
    /**
     * 分页查询tag标签
     * @param page
     * @param rows
     * @return
     */
    PageResult<Tag> findTags(Integer page, Integer rows);

    /**
     * 添加Tag标签
     * @param tag
     * @return
     */
    Result addTag(Tag tag);

    // 修改tag标签
    Result editTag(Tag tag);

    // 删除tag标签
    Result delTag(Long id);
}
