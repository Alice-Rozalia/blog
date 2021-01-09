package com.kuro.service;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.model.entity.Diary;

public interface DiaryService {
    /**
     * 分页查询日记记录，不传参数则查询所有
     * @param page
     * @param rows
     * @return
     */
    PageResult<Diary> findDairys(Integer page, Integer rows);

    // 根据id查找日记记录
    Result findDiaryById(Long id);

    // 新增日记记录
    Result addDiary(String content);

    // 修改日记记录
    Result editDiary(Diary diary);

    // 删除日记记录
    Result delDiary(Long id);
}
