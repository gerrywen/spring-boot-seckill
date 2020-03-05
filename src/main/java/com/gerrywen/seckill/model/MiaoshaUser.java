package com.gerrywen.seckill.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class MiaoshaUser implements Serializable {
    @ApiModelProperty(value = "用户id, 手机号码")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "MD5(MD5(password+salt)+salt)")
    private String password;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "头像, 云存储id")
    private String head;

    @ApiModelProperty(value = "注册时间")
    private Date redisterDate;

    @ApiModelProperty(value = "上次登录时间")
    private Date lastLoginDate;

    @ApiModelProperty(value = "总登录次数")
    private Integer loginCount;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getRedisterDate() {
        return redisterDate;
    }

    public void setRedisterDate(Date redisterDate) {
        this.redisterDate = redisterDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", nickname=").append(nickname);
        sb.append(", password=").append(password);
        sb.append(", salt=").append(salt);
        sb.append(", head=").append(head);
        sb.append(", redisterDate=").append(redisterDate);
        sb.append(", lastLoginDate=").append(lastLoginDate);
        sb.append(", loginCount=").append(loginCount);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}