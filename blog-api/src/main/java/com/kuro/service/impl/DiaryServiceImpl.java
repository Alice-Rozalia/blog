package com.kuro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.mapper.DiaryMapper;
import com.kuro.model.entity.Diary;
import com.kuro.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    /**
     * 分页查询日记记录，不传参数则查询所有
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<Diary> findDairys(Integer page, Integer rows) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        List<Diary> diaries = this.diaryMapper.selectAll();

        PageInfo<Diary> pageInfo = new PageInfo<>(diaries);
        return new PageResult<>(pageInfo.getTotal(), diaries);
    }

    @Override
    public Result findDiaryById(Long id) {
        Diary diary = this.diaryMapper.selectByPrimaryKey(id);
        if (diary == null) {
            return Result.custom(ResultCode.DIARY_NOT_EXIST);
        }
        return Result.ok().data("diary", diary);
    }

    @Override
    public Result addDiary(String content) {
        Diary diary = new Diary();
        diary.setCreateTime(System.currentTimeMillis());
        diary.setContent(content);
        int result = this.diaryMapper.insertSelective(diary);
        if (result == 0) {
            return Result.custom(ResultCode.ADD_ERROR);
        }
        return Result.custom(ResultCode.ADD_SUCCESS);
    }

    @Override
    public Result editDiary(Diary diary) {
        int result = this.diaryMapper.updateByPrimaryKeySelective(diary);
        if (result == 0) {
            return Result.custom(ResultCode.UPDATE_ERROR);
        }
        return Result.custom(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result delDiary(Long id) {
        int result = this.diaryMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            return Result.custom(ResultCode.DELETE_ERROR);
        }
        return Result.custom(ResultCode.DELETE_SECCESS);
    }
}
