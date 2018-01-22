package com.fiki.app.wifi.wifidocumentation.src.domain.model;

import java.io.Serializable;

/**
 * Created by root on 2017/10/14.
 */

public class EmailDetails implements Serializable {

    private String email;
    private String password;
    private Integer id;

    public EmailDetails(Builder builder)
    {
        this.email = builder.email;
        this.password = builder.password;
        this.id = builder.id;
    }

    //builder
    public static class Builder{
        private String email;
        private String password;
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
        public Builder password(String password)
        {
            this.password = password;
            return this;
        }

        public Builder copy(EmailDetails value)
        {
            this.email = value.getEmail();
            this.password = value.getPassword();
            this.id = value.getId();

            return this;
        }

        public EmailDetails build(){
            return new EmailDetails(this);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }
}
