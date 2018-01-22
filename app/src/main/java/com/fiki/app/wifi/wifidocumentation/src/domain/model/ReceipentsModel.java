package com.fiki.app.wifi.wifidocumentation.src.domain.model;

import java.io.Serializable;

/**
 * Created by fiki on 2017/11/23.
 */

public class ReceipentsModel implements Serializable {

    private String email;
    private Integer id;

    public ReceipentsModel(Builder builder)
    {
        this.email = builder.email;
        this.id = builder.id;
    }

    //builder
    public static class Builder{
        private String email;
        private Integer id;

        public Builder email(String email)
        {
            this.email = email;
            return this;
        }
        public Builder id(Integer id)
        {
            this.id = id;
            return this;
        }

        public Builder copy(ReceipentsModel value)
        {
            this.email = value.getEmail();
            this.id = value.getId();

            return this;
        }
        public ReceipentsModel build(){
            return new ReceipentsModel(this);
        }
    }

    public String getEmail() {
        return email;
    }
    public Integer getId() {
        return id;
    }
}
