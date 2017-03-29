
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package net.labthink.cvs;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Mosliu
 */
public class ControlFileList {
    static StringBuilder sb  = new StringBuilder();
    String               dir = "";

    public String _serachFiles(String dir) {
        File root = new File(dir);

        // 需要写校验。
        File[]   filesOrDirs = root.listFiles();
        String[] result      = new String[filesOrDirs.length];

        for (int i = 0; i < filesOrDirs.length; i++) {
            if (filesOrDirs[i].isDirectory()) {
                serachFiles(filesOrDirs[i].getAbsolutePath());

//              temp += filesOrDirs[i].getName() + ",";
                sb.append(filesOrDirs[i].getName());
                sb.append(",");

//              } else {
//                  result[i] = filesOrDirs[i].getName();
//                  temp += filesOrDirs[i].getName() + ",";
            }
        }

        return sb.toString();
    }

    public String[] serachFiles(String dir) {
        String tmp = _serachFiles(dir);

        return tmp.split(",");
    }

    public static void main(String[] args) {
        String a[] = new ControlFileList().serachFiles("f:/cvsroot");
    }
}


