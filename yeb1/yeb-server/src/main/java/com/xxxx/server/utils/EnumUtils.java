package com.xxxx.server.utils;


import com.xxxx.server.enums.BaseEnum;
import org.apache.commons.lang3.StringUtils;

public class EnumUtils {

    /**
     * 判断枚举名是否存在于指定枚举数组中
     *
     * @param enums 枚举数组
     * @param name  枚举名
     * @return boolean
     */
    public static boolean isExist(BaseEnum[] enums, String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        for (BaseEnum e : enums) {
            if (name.equals(e.getName())) {
                return true;
            }
        }
        return false;
    }
}