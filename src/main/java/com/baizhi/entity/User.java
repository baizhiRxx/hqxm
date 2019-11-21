package com.baizhi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    @Id
    private String id;
    private String photo;
    private String name;
    @Column(name="nickName")
    private String nickName;
    private String password;
    private String salt;
    private String gender;
    private String sign;
    private String location;
    @Column(name="phoneNumber")
    private String phoneNumber;
    @Column(name="registTime")
    private Date registTime;
    @Transient
    private List<Role> roles;
}
