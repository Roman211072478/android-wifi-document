package com.fiki.app.wifi.wifidocumentation;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by root on 2017/10/14.
 */

public class DataObjectTest {
    @Test
    public void TestObject() throws Exception {
        MyData data = new MyData.Builder()
                .macAddress("mac")
                .portNo("name")
                .serialNo("bb")
                .switchNo("aaa")
                .build();

        Assert.assertEquals(data.getMacAddress(),"mac");
        Assert.assertEquals(data.getPortNo(),"name");
        Assert.assertEquals(data.getSerialNo(),"bb");
        Assert.assertEquals(data.getSwitchNo(),"aaa");
    }

    @Test
    public void stringArray() throws Exception {
        String[] list;

        list= new String[]{"name","fiki"};
        Assert.assertEquals(list.length,2);
        Assert.assertEquals(list[0],"name");
        Assert.assertEquals(list[1],"fiki");

    }
}
