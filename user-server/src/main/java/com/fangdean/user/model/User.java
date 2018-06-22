package com.fangdean.user.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class User {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String aboutme;
    private String passwd;
    private String confirmPasswd;
    private Integer type;
    private Date createTime;
    private Integer enable;

    private String avatar;

    @JSONField(deserialize = false, serialize = false)
    private MultipartFile avatarFile;

    private String newPassword;

    private String key;

    private Integer agencyId;

    private String token;

    private String enableUrl;

    private String agencyName;
}
