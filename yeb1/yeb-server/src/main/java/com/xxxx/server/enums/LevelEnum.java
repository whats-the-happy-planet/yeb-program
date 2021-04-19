package com.xxxx.server.enums;

public enum LevelEnum  implements BaseEnum{
    /**
     * 正高级
     */
    SENIOR(0, "正高级"),
    /**
     * 副高级
     */
    SUB_SENIOR(1, "副高级"),
    /**
     * 中级
     */
    MEDIUM(2, "中级"),
    /**
     * 初级
     */
    ELEMENTARY(3, "初级"),
    /**
     * 员级
     */
    CAGT(4, "员级");


    private Integer value;
    private String name;


    private LevelEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
