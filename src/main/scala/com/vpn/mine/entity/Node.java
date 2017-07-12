package com.vpn.mine.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by coder on 17-7-1.
 */
public class Node implements Serializable{

    //节点id
    private int nid;


    //节点名字
    @SerializedName("node_name")
    private String nodeName;

    //返回IP地址
    private String address;

    //节点备注
    @SerializedName("node_info")
    private String nodeInfo;

    //区域
    private String area;

    //暂无
    private String status;

    //暂无
    private String visible;

    //连接密码
    private String passwd;

    //端口
    private String port;

    //加密方式
    private String method;

    //协议
    private String protocol;

    //混淆
    private String pbfs;

    //0为不可连接，1为可连接,选择的时候提示VIP专项线路，当前用户等级不可连接
    private int level;

    public Node() {
    }

    public Node(int nid, String nodeName, String address, String nodeInfo, String area, String status, String visible, String password, String port, String method, String protocol, String pbfs, int level) {
        this.nid = nid;
        this.nodeName = nodeName;
        this.address = address;
        this.nodeInfo = nodeInfo;
        this.area = area;
        this.status = status;
        this.visible = visible;
        this.passwd = password;
        this.port = port;
        this.method = method;
        this.protocol = protocol;
        this.pbfs = pbfs;
        this.level = level;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(String nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getPassword() {
        return passwd;
    }

    public void setPassword(String password) {
        this.passwd = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPbfs() {
        return pbfs;
    }

    public void setPbfs(String pbfs) {
        this.pbfs = pbfs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
