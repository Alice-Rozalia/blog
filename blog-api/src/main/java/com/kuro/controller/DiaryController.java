package com.kuro.controller;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.common.handler.BusinessException;
import com.kuro.model.entity.Diary;
import com.kuro.service.impl.DiaryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "日记管理")
public class DiaryController {

    @Autowired
    private DiaryServiceImpl diaryService;

    @GetMapping("/pub/diary/list")
    @ApiOperation(value = "查询日记", notes = "分页查询所有的日记记录，不传参数则查询所有")
    public Result findDiarys(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows
    ) {
        PageResult<Diary> result = this.diaryService.findDairys(page, rows);
        return Result.ok().data("diarys", result);
    }

    @GetMapping("/pri/diary/find_by_id")
    @ApiOperation(value = "查找日记", notes = "根据id查找日记记录")
    public Result findDiaryById(@RequestParam Long id) {
        return this.diaryService.findDiaryById(id);
    }

    @RequiresPermissions({"diary:add"})
    @PostMapping("/pri/diary/add")
    @ApiOperation(value = "添加日记", notes = "新增一条日记记录")
    public Result addDiary(@RequestParam @Validated String content) {
        if (content == null || content == "") {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.diaryService.addDiary(content);
    }

    @RequiresPermissions({"diary:edit"})
    @PutMapping("/pri/diary/edit")
    @ApiOperation(value = "修改日记", notes = "根据id修改日记记录")
    public Result editDiary(@RequestBody @Validated Diary diary) {
        if (diary == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.diaryService.editDiary(diary);
    }

    @RequiresPermissions({"diary:delete"})
    @DeleteMapping("/pri/diary/delete")
    @ApiOperation(value = "删除日记", notes = "根据id删除日记记录")
    public Result deleteDiary(@RequestParam Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_NOT_VALID.getCode(), ResultCode.PARAM_NOT_VALID.getMessage());
        }
        return this.diaryService.delDiary(id);
    }
}
