package com.fiki.app.wifi.wifidocumentation.src.domain.model;

import java.io.Serializable;

/**
 * Created by root on 2017/10/14.
 */

public class MyData implements Serializable {
    private String macAddress;
    private String serialNo;
    private String portNo;
    private String switchNo;
    private String location;
    private Integer id;

    public MyData(Builder builder)
    {
        this.macAddress = builder.macAddress;
        this.serialNo = builder.serialNo;
        this.portNo = builder.portNo;
        this.location = builder.location;
        this.switchNo = builder.switchNo;
        this.id = builder.id;
    }

    //builder
    public static class Builder{
        private String macAddress;
        private String serialNo;
        private String portNo;
        private String location;
        private String switchNo;
        private Integer id;

        public Builder macAddress(String macAddress)
        {
            this.macAddress = macAddress;
            return this;
        }  public Builder location(String location)
        {
            this.location = location;
            return this;
        }
        public Builder id(Integer id)
        {
            this.id = id;
            return this;
        }
        public Builder serialNo(String serialNo)
        {
            this.serialNo = serialNo;
            return this;
        }
        public Builder portNo(String portNo)
        {
            this.portNo = portNo;
            return this;
        }
        public Builder switchNo(String switchNo)
        {
            this.switchNo = switchNo;
            return this;
        }

        public Builder copy(MyData value)
        {
            this.macAddress = value.getMacAddress();
            this.serialNo = value.getSerialNo();
            this.portNo = value.getPortNo();
            this.switchNo = value.getSwitchNo();
            this.location = value.getLocation();
            this.id = value.getId();

            return this;
        }

        public MyData build(){
            return new MyData(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getPortNo() {
        return portNo;
    }

    public String getLocation() {
        return location;
    }

    public String getSwitchNo() {
        return switchNo;
    }
}
