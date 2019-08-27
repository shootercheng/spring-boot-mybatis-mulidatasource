package com.example.demo.entity;

import java.util.List;

/**
 * @author chengdu
 * @date 2019/7/22.
 */
public class Tree {

    private Integer id;

    private Integer parentId;

    public Tree(Integer id, Integer parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getParentId() {
        return parentId;
    }
}
