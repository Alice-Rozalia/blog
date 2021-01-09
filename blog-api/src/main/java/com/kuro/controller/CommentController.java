package com.kuro.controller;

import com.kuro.common.entity.PageResult;
import com.kuro.common.entity.Result;
import com.kuro.common.entity.ResultCode;
import com.kuro.model.entity.Comment;
import com.kuro.model.entity.Message;
import com.kuro.model.vo.CMCountVo;
import com.kuro.model.vo.CommentVo;
import com.kuro.model.vo.MessageVo;
import com.kuro.service.impl.CommentServiceImpl;
import com.kuro.service.impl.MessageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Api(value = "系统管理模块", tags = "留言评论")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private MessageServiceImpl messageService;

    @GetMapping("/pub/comment/list_by_aid")
    @ApiOperation(value = "查询评论", notes = "分页查询所有的评论，不传参数则查询所有")
    public Result findComments(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "aid", required = false) Long aid
    ) {
        if (aid == null) {
            return Result.custom(ResultCode.PARAM_NOT_COMPLETE);
        }
        PageResult<CommentVo> comments = this.commentService.findCommentsByArticleId(page, rows, aid);
        return Result.ok().data("comments", comments);
    }

    @GetMapping("/pri/comment/list")
    @ApiOperation(value = "评论刘表", notes = "分页查询所有的评论（无处理）")
    public Result findCommentList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<Comment> comments = this.commentService.findComments(page, rows);
        return Result.ok().data("comment_list", comments);
    }

    @GetMapping("/pub/message/list")
    @ApiOperation(value = "查询留言", notes = "分页查询所有的留言（树形结构）")
    public Result findMessages(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<MessageVo> messages = this.messageService.findMessages(page, rows);
        return Result.ok().data("messages", messages);
    }

    @GetMapping("/pri/message/primeval")
    @ApiOperation(value = "留言刘表", notes = "分页查询所有的留言（无处理）")
    public Result findMessageList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<Message> messageList = this.messageService.findMessageList(page, rows);
        return Result.ok().data("message_list", messageList);
    }

    @RequiresPermissions({"comment:add"})
    @PostMapping("/pri/comment/add")
    @ApiOperation(value = "新增评论", notes = "新增评论")
    public Result addComment(@RequestBody @Validated Comment comment) {
        return this.commentService.addComment(comment);
    }

    @RequiresPermissions({"message:add"})
    @PostMapping("/pri/message/add")
    @ApiOperation(value = "新增留言", notes = "新增留言")
    public Result addMessage(@RequestBody @Validated Message message) {
        return this.messageService.addMessage(message);
    }

    @RequiresPermissions({"comment:edit"})
    @PutMapping("/pri/comment/edit")
    @ApiOperation(value = "修改评论", notes = "根据id修改评论")
    public Result editComment(@RequestBody @Validated Comment comment) {
        return null;
    }

    @RequiresPermissions({"comment:edit"})
    @PutMapping("/pri/message/edit")
    @ApiOperation(value = "修改留言", notes = "根据id修改留言")
    public Result editMessage(@RequestBody @Validated Message message) {
        return null;
    }

    @RequiresPermissions({"comment:delete"})
    @DeleteMapping("/pri/comment/delete")
    @ApiOperation(value = "删除评论", notes = "根据id删除评论")
    public Result deleteComment(@RequestParam(value = "id", required = false) Long id) {
        return this.commentService.deleteComment(id);
    }

    @RequiresPermissions({"message:delete"})
    @DeleteMapping("/pri/message/delete")
    @ApiOperation(value = "删除留言", notes = "根据id删除留言")
    public Result deleteMessage(@RequestParam(value = "id", required = false) Long id) {
        return this.messageService.deleteMessage(id);
    }

    @GetMapping("/pub/cm/count")
    @ApiOperation(value = "统计数量", notes = "获取留言和评论的总数")
    public Result count() {
        CMCountVo countVo = this.messageService.statistics();
        return Result.ok().data("cm_count", countVo);
    }
}
