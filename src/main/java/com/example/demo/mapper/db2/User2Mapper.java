package com.example.demo.mapper.db2;

import com.example.demo.entity.User;
import java.util.List;

public interface User2Mapper {

    List<User> getAll();

    User getOne(Long id);

    int insert(User user);

    void update(User user);

    void delete(Long id);

}