/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 分别获得单文件或多重文件后缀的filefilter
 * @author Mosliu
 */
public class ExtensionFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

    private String extension;
    private String description;
    private boolean multiexts = false;
    private boolean accaptdirs = false;
    private HashSet<String> extensions = new HashSet<String>();

    //单后缀
    public ExtensionFileFilter(String _extension) {
        this.extension = _extension;
    }
    //多后缀，使用分号;；逗号,，顿号、斜线\/分隔

    public ExtensionFileFilter(String _extension, boolean multiple) {
        this(_extension,multiple,false);
    }
    public ExtensionFileFilter(String _extension, boolean multiple,boolean dirs) {
        this.extension = _extension;
        multiexts = multiple;
        accaptdirs=dirs;
        String[] strlist = extension.split(",|;|、|，|；|\\|/");
        for (int i = 0; i < strlist.length; i++) {
            String string = strlist[i];
            if (string != null && string.length() > 0) {
                extensions.add(string.toLowerCase());
            }
        }
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return accaptdirs;
        }
        String name = file.getName();
// find the last
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return false;
        } else if (index == name.length() - 1) {
            return false;
        }

        if (!multiexts) {
            return acceptSingle(name);
        } else {
            return acceptMultiple(name);
        }
    }

    public boolean acceptMultiple(String name) {
        String suffix = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        if (extensions.contains(suffix)) {
            return true;
        }
        return false;
    }

    public boolean acceptSingle(String name) {
        return this.extension.equalsIgnoreCase(name.substring(name.lastIndexOf(".") + 1));
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder(description);
        sb.append("(");
        if (multiexts) {
            for (Iterator<String> it = extensions.iterator(); it.hasNext();) {
                String string = it.next();
                sb.append(string).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            sb.append(extension);
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
}
