package com.kuro.model.vo;

import com.kuro.model.entity.Message;
import lombok.Data;

import java.util.List;

@Data
public class MessageVo extends Message {

    private String nickname;

    private String avatar;

    private List<MessageVo> children;
}
