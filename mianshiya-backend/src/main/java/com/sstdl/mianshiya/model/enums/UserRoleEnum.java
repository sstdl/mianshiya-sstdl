package com.sstdl.mianshiya.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SSTDL
 * @description 用户角色枚举
 */
public enum UserRoleEnum {
    USER("用户", "user"),
    ADMIN("管理员", "admin"),
    BAN("被封号", "ban");

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
    public String getText() {return text;}
    public String getValue() {return value;}

    /**
     * 获取值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum item : UserRoleEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
