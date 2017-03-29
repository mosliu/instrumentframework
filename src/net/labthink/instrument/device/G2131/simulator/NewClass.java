/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.G2131.simulator;

import java.io.File;

/**
 *
 * @author Moses
 */
public class NewClass {

    public static void main(String[] args) {
        File path = new File("E:\\info\\图标");
//        File file = new File("E:\\info\\图标\\图标仓库.exe");
//        System.out.println(path.getName());
//        System.out.println(path.getAbsolutePath());
//        System.out.println(file.getName());
//        System.out.println(file.getAbsolutePath());
//        file.renameTo(new File(file.getAbsolutePath().replaceAll("exe", "exe.exe")));

        parsePath(path);

    }

    public static void parsePath(File path) {
        if (path.isDirectory()) {
            File paths[] = path.listFiles();
            if (paths != null) {
                for (int i = 0; i < paths.length; i++) {
                    File file = paths[i];
                    parsePath(file);
                }
            }
//            parseFile(path);
        } else {
        }
            parseFile(path);
    }

    public static void parseFile(File f) {
        String pathname = f.getAbsolutePath();
        String replacename = pathname.replaceAll("嗾", "图").replaceAll("篻", "系")
                .replaceAll("総", "经").replaceAll("秋体", "立体").replaceAll("媃", "他")
                .replaceAll("諞", "诞").replaceAll("緿", "绿").replaceAll("_", "件")
                .replaceAll("鎮", "钮").replaceAll("擏", "族").replaceAll("菝", "蓝")
                .replaceAll("鷑", "黑").replaceAll("撇", "文").replaceAll("撙", "料")
                .replaceAll("冡", "务").replaceAll("暜", "果").replaceAll("俏", "像")
                .replaceAll("韎", "风").replaceAll("擧", "旧").replaceAll("総", "经")
                .replaceAll("緜", "页").replaceAll("裈", "览").replaceAll("菝", "蓝")
                .replaceAll("綹", "纹").replaceAll("媃", "他").replaceAll("緟", "列")
                .replaceAll("掭", "播").replaceAll("疮", "斑").replaceAll("郑", "金")
                .replaceAll("澸", "炸").replaceAll("沂", "涂").replaceAll("胪", "天")
                .replaceAll("墆", "妆").replaceAll("鞘", "题").replaceAll("嚋", "形")
                .replaceAll("冨", "动").replaceAll("綿", "线").replaceAll("鏶", "灰")
                .replaceAll("羖", "肖").replaceAll("斖", "亮").replaceAll("痒", "盒")
                .replaceAll("峥", "工").replaceAll("軐", "运").replaceAll("隋", "鞋")
                .replaceAll("綢", "红").replaceAll("箉", "粉").replaceAll("暁", "阳")
                .replaceAll("嗽", "国").replaceAll("擗", "旗").replaceAll("貳", "足")
                .replaceAll("磍", "种").replaceAll("撯", "斯").replaceAll("痘", "盘")
//        String replacename = pathname
                .replaceAll("必", "情").replaceAll("僉", "爽").replaceAll("韟", "食")
                .replaceAll("叁", "品").replaceAll("厌", "与").replaceAll("勯", "可")
                .replaceAll("膱", "花").replaceAll("臹", "苹");
        f.renameTo(new File(replacename));

    }
}
