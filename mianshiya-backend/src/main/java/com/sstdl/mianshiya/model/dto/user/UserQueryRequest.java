package com.sstdl.mianshiya.model.dto.user;

import com.sstdl.mianshiya.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询用户请求
 */
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
