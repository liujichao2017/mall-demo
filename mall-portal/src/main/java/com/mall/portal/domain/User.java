package com.mall.portal.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "用户对象")
public class User {
    @ApiModelProperty(value = "用户ID", name = "id", required = true, example = "111" )
    private Long id;

    @ApiModelProperty(value = "用户姓名", name = "name", required = true, example = "zhangsan" )
    private String name;

    @ApiModelProperty(value = "用户年龄", name = "age", required = true, example = "22" )
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
