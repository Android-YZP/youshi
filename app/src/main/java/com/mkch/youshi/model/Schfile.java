package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Smith on 2016/9/9.
 */
@Table(name="schfile")
public class Schfile {
    /**
     * 主键自增长ID
     */
    @Column(name = "id",isId = true,autoGen = true)//主键自增长ID
    private int id;
    /**
     * 文件类型,(文件后缀)
     */
    @Column(name = "type")
    private String type;
    /**
     * 文件名
     */
    @Column(name = "name")
    private String name;
    /**
     * 云盘文件编号
     */
    @Column(name = "cloudid")
    private int cloudid;
    /**
     * 附件本地地址
     */
    @Column(name = "local_address")
    private String local_address;
    /**
     * 附件大小
     */
    @Column(name = "size")
    private String size;
    /**
     * 附件创建时间
     */
    @Column(name = "create_time")
    private String create_time;
    /**
     * 附件到期时间
     */
    @Column(name = "expire_time")
    private String expire_time;
    /**
     * 外键ID所属日程
     */
    @Column(name = "sid")
    private int sid;

    @Override
    public String toString() {
        return "schfile{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", cloudid=" + cloudid +
                ", local_address='" + local_address + '\'' +
                ", size='" + size + '\'' +
                ", create_time='" + create_time + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", sid=" + sid +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCloudid() {
        return cloudid;
    }

    public void setCloudid(int cloudid) {
        this.cloudid = cloudid;
    }

    public String getLocal_address() {
        return local_address;
    }

    public void setLocal_address(String local_address) {
        this.local_address = local_address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
