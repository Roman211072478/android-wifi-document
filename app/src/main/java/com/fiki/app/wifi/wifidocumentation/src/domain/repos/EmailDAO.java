package com.fiki.app.wifi.wifidocumentation.src.domain.repos;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;

import java.util.HashMap;

/**
 * Created by root on 2017/10/14.
 */

public interface EmailDAO {
    public Long create(EmailDetails object);
    public int update(EmailDetails object);
    public int delete(EmailDetails object);
    public int deleteTable();
    public EmailDetails findById(Integer id);
    public HashMap getList();

}
