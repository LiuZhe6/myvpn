package com.vpn.mine.utils;

import com.vpn.mine.entity.Node;
import com.vpn.mine.entity.User;

import java.util.ArrayList;

/**
 * Created by coder on 17-7-2.
 */
public class DataSaver {

    //节点序号
    public static int NODE_INDEX = 0;

    //user
    public static User USER = null;

    //选项改变标记
    public static boolean CHANGED = false;

    //节点选择(用于connect页面显示)
    public static String NODE_NAME ;

    //节点列表
    public static ArrayList<Node> NODES ;
}
