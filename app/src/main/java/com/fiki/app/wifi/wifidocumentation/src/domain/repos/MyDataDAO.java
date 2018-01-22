package com.fiki.app.wifi.wifidocumentation.src.domain.repos;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;

import java.util.HashMap;

/**
 * Created by root on 2017/10/14.
 */

public interface MyDataDAO {
    public Long create(MyData object);
    public int update(MyData object);
    public int delete(MyData object);
    public int deleteTable();
    public MyData findById(Integer id);
    public HashMap getList();
}
