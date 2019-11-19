package com.baizhi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "admin")
public class Admin implements Serializable {
    @Id
    private String id;
    private String username;
    private String password;
}
