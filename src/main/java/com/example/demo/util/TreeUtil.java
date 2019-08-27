package com.example.demo.util;


import com.example.demo.entity.Tree;
import com.example.demo.entity.TreeResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengdu
 * @date 2019/7/22.
 */
public class TreeUtil {

    public static void main(String[] args){
        // create tree
        Tree root = new Tree(1,null);
        Tree tree21 = new Tree(2, 1);
        Tree tree31 = new Tree(3, 1);
        Tree tree41 = new Tree(4, 1);
        Tree tree52 = new Tree(5,2);
        Tree tree62 = new Tree(6,2);
        Tree tree73 = new Tree(7, 3);
        Tree tree84 = new Tree(8, 4);
        List<Tree> treeList = new ArrayList<>();
        treeList.add(root);
        treeList.add(tree21);
        treeList.add(tree31);
        treeList.add(tree41);
        treeList.add(tree52);
        treeList.add(tree62);
        treeList.add(tree73);
        treeList.add(tree84);
        // parentId --> id
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Integer> rootIds = new ArrayList<>(10);
        for(Tree tree : treeList){
            Integer parentId = tree.getParentId();
            Integer id = tree.getId();
            if(parentId == null){
                rootIds.add(id);
                continue;
            }
            if(!map.containsKey(parentId)){
                List<Integer> list = new ArrayList<>();
                map.put(parentId, list);
            }
            map.get(parentId).add(id);
        }
        System.out.println(map);
        // level1 -> level3
        List<TreeResult> treeResults = new ArrayList<>();
        for(Integer rootId : rootIds){
            if(map.containsKey(rootId)){
                List<TreeResult> childs = new ArrayList<>();
                TreeResult treeResult = new TreeResult(rootId, null, childs);
                List<Integer> levelTwoIds = map.get(rootId);
                for(Integer level2Id : levelTwoIds){
                    if(map.containsKey(level2Id)){
                        List<Integer> levelThreeIds = map.get(level2Id);
                        for(Integer level3Id : levelThreeIds){
                            TreeResult threTree = new TreeResult(level3Id, level2Id, new ArrayList<>());
                            childs.add(threTree);
                        }
                    }
                }
                treeResults.add(treeResult);
            }
        }
        System.out.println(treeResults);
    }
}
