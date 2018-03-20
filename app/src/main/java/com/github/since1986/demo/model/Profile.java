package com.github.since1986.demo.model;

import java.io.Serializable;

public class Profile implements Serializable {

    private String username;
    private Long userId;
    private String name;
    private String gender;
    private String birthday;
    private String email;
    private String phone;

    public Profile(){}

    private Profile(Builder builder) {
        setUsername(builder.username);
        setUserId(builder.userId);
        setName(builder.name);
        setGender(builder.gender);
        setBirthday(builder.birthday);
        setEmail(builder.email);
        setPhone(builder.phone);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static final class Builder {
        private String username;
        private Long userId;
        private String name;
        private String gender;
        private String birthday;
        private String email;
        private String phone;

        private Builder() {
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder withBirthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Profile build() {
            return new Profile(this);
        }
    }
}
