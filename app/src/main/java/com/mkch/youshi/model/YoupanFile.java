package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Smith on 2016/10/27.
 */
@Table(name = "youpanfile")
public class YoupanFile {

    /**
     * 主键自增长ID
     */
    @Column(name = "id", isId = true, autoGen = true)//主键自增长ID
    private int id;

    /**
     * 文件类型（1：文档，2：相册，3：视频，4：音频，5：其他）
     */
    @Column(name = "type")
    private int type;

    /**
     * 文件名
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件后缀
     */
    @Column(name = "suf")
    private String suf;

    /**
     * 文件的本地地址
     */
    @Column(name = "local_address")
    private String local_address;

    /**
     * 创建的时间
     */
    @Column(name = "create_time")
    private String create_time;

    /**
     * 服务器的文件下载地址
     */
    @Column(name = "server_address")
    private String server_address;

    @Override
    public String toString() {
        return "YoupanFile{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", suf='" + suf + '\'' +
                ", local_address=" + local_address +
                ", create_time=" + create_time +
                ", server_address=" + server_address +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuf() {
        return suf;
    }

    public void setSuf(String suf) {
        this.suf = suf;
    }

    public String getLocal_address() {
        return local_address;
    }

    public void setLocal_address(String local_address) {
        this.local_address = local_address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getServer_address() {
        return server_address;
    }

    public void setServer_address(String server_address) {
        this.server_address = server_address;
    }
}
