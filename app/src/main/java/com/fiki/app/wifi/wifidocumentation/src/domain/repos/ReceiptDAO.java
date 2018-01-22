package com.fiki.app.wifi.wifidocumentation.src.domain.repos;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ReceipentsModel;

import java.util.HashMap;

/**
 * Created by fiki on 2017/11/23.
 */

public interface ReceiptDAO {
    public Long create(ReceipentsModel object);
    public int update(ReceipentsModel object);
    public int delete(ReceipentsModel object);
    public int deleteTable();
    public ReceipentsModel findById(Integer id);
    public HashMap getList();
}
