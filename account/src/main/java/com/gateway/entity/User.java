package com.gateway.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User {
    public long id;
    public String username;
    public String password;
}
