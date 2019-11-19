package com.baizhi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Banner implements Serializable {
    @Id
    private String id;
    private String title;
    private String url;
    private String status;
    @JSONField(format = "yyyy-MM-dd")
    private Date create_date;
    private String description;
}
