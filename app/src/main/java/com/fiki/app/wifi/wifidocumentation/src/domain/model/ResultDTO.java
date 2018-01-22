package com.fiki.app.wifi.wifidocumentation.src.domain.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by root on 2017/10/16.
 */

public class ResultDTO implements Serializable {
    //this class variables
    private String request;
    private String sResult;
    private HashMap result;

    //private constructor to remove the use of (new ResultDTO();)
    private ResultDTO()
    {
    }
    //public constructor
    public ResultDTO(Builder builder)
    {
        this.request = builder.request;
        this.result = builder.result;
        this.sResult = builder.sResult;
    }

    //builder
    public static class Builder{
        //builder variables
        private String request;
        private HashMap result;
        private String sResult;


        public Builder sResult(String sResult)
        {
            this.sResult = sResult;
            return this;
        }

        public Builder request(String request)
        {
            this.request = request;
            return this;
        }
        public Builder result(HashMap result)
        {
            this.result = result;
            return this;
        }

        public Builder copy(ResultDTO value)
        {
            this.result = value.getResult();
            this.request = value.getRequest();
            this.sResult = value.getSResult();

            return this;
        }

        public ResultDTO build(){
            return new ResultDTO(this);
        }
    }

    //getters
    public String getRequest() {
        return request;
    }
    public HashMap getResult() {
        return result;
    }
    public String getSResult() {
        return sResult;
    }
}
