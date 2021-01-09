package com.kuro.model.vo;

import com.kuro.model.entity.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVo extends Menu {

    private List<Menu> children = new ArrayList<>();

}
