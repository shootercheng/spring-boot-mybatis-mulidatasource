package com.example.demo.entity;

import java.util.List;

/**
 * @author chengdu
 * @date 2019/7/23.
 */
public class TreeResult {

    private Integer id;

    private Integer parentId;

    private List<TreeResult> childs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<TreeResult> getChilds() {
        return childs;
    }

    public void setChilds(List<TreeResult> childs) {
        this.childs = childs;
    }

    public TreeResult(Integer id, Integer parentId, List<TreeResult> childs) {
        this.id = id;
        this.parentId = parentId;
        this.childs = childs;
    }
}
