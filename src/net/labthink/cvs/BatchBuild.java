/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.cvs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.FilePlus;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author Mosliu
 */
public class BatchBuild {

    public static void main(String[] args) {
        dobatch();
    }

    private static void dobatch() {
        String namefile = "d:/device.txt";
        String rootfolder = "d:/tempfile/cvs/";
        String[] namelist = readDeviceName(namefile);
        StringBuilder sb = new StringBuilder();

        if (namelist != null) {
            for (int i = 0; i < namelist.length; i++) {
                String devicename = namelist[i];
                createfolder(rootfolder, devicename);
                sb.append(devicename).append(":").append("\r\n");
                sb.append(devicename).append("electric:").append("\r\n");
                sb.append(devicename).append("mechinic:").append("\r\n");
                sb.append(devicename).append("documents:").append("\r\n");
                sb.append(devicename).append("sourcecode:").append("\r\n");
            }
            System.out.println(sb);
        }
    }

    /**
     * 从文件中获取设备名字列表
     * @param namefile
     * @return
     */
    private static String[] readDeviceName(String namefile) {
        File _f = new File(namefile);
        if (!_f.exists()) {
            return null;
        }
        String a = FilePlus.ReadTextFileToStringLn(namefile).replaceAll("\\|/|-|/", "");

//        System.out.println("a = " + a);
        String list[] = a.split("\r\n|\n");
//        System.out.println(list.length);
//        for (int i = 0; i < list.length; i++) {
//            String st = list[i];
//            System.out.println((i + 1) + ":" + st);
//        }
        return list;
    }

    /**
     * 在指定目录下增加文件夹
     * @param rootfolder 指定目录
     * @param devicename 文件夹名
     */
    private static void createfolder(String rootfolder, String devicename) {
        if (!rootfolder.endsWith("/|\\")) {
            rootfolder += "/";
        }
        String path = rootfolder + devicename;
        String patha = path + "/CVS";
        File _f = new File(patha);
        _f.mkdirs();
        List<CvsAcl> cvssacllist = new ArrayList<CvsAcl>();
        CvsAcl ac = new CvsAcl();
        ac.set("deny");
        cvssacllist.add(ac);
        ac = new CvsAcl("root");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl("manager");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl(devicename);
        ac.set("read");
        cvssacllist.add(ac);
        createCvsXml(patha, cvssacllist);
        createDefaultTxtFile(patha, devicename);
//        createDefaultCvsXml(patha);


        //==========================================================
        patha = path + "/电气/CVS";
        _f = new File(patha);
        _f.mkdirs();
        cvssacllist = new ArrayList<CvsAcl>();
        ac = new CvsAcl();
        ac.set("deny");
        cvssacllist.add(ac);
        ac = new CvsAcl("root");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl("manager");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl(devicename + "electric");
        ac.set("read");
        ac.set("write");
        ac.set("tag");
        ac.set("create");
        cvssacllist.add(ac);
        createCvsXml(patha, cvssacllist);
//        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/电气/PCB图/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中文程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中文程序/定制程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中文程序/历史程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中文程序/现用程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中文程序/其他/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/电气/单片机程序/英文程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/英文程序/定制程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/英文程序/历史程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/英文程序/现用程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/英文程序/其他/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/电气/单片机程序/中英文合版程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中英文合版程序/定制程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中英文合版程序/历史程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中英文合版程序/现用程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/单片机程序/中英文合版程序/其他/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/电气/说明文档/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/电气/原理图/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        //==========================================================
        patha = path + "/机械/CVS";
        _f = new File(patha);
        _f.mkdirs();
        cvssacllist = new ArrayList<CvsAcl>();
        ac = new CvsAcl();
        ac.set("deny");
        cvssacllist.add(ac);
        ac = new CvsAcl("root");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl("manager");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl(devicename + "mechinic");
        ac.set("read");
        ac.set("write");
        ac.set("tag");
        ac.set("create");
        cvssacllist.add(ac);
        createCvsXml(patha, cvssacllist);
//        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/机械/其它/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        patha = path + "/机械/客户定制/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/机械/图纸资料/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/机械/图纸资料/设备图纸/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/机械/图纸资料/丝网印刷/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/机械/图纸资料/文字文档用途/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        //==========================================================
        patha = path + "/技术资料/CVS";
        _f = new File(patha);
        _f.mkdirs();
        cvssacllist = new ArrayList<CvsAcl>();
        ac = new CvsAcl();
        ac.set("deny");
        cvssacllist.add(ac);
        ac = new CvsAcl("root");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl("manager");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl(devicename + "documents");
        ac.set("read");
        ac.set("write");
        ac.set("tag");
        ac.set("create");
        cvssacllist.add(ac);
        createCvsXml(patha, cvssacllist);
//        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
//        patha = path + "/技术资料/标准/CVS";
//        _f = new File(patha);
//        _f.mkdirs();
//        createDefaultCvsXml(patha);
//        createDefaultTxtFile(patha, devicename);
//        patha = path + "/技术资料/软件开发文档/CVS";
//        _f = new File(patha);
//        _f.mkdirs();
//        createDefaultCvsXml(patha);
//        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/生产资料/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/生产资料/检定规程/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/生产资料/明细表/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
//        patha = path + "/技术资料/生产资料/明细表/钣金件明细表/CVS";
//        _f = new File(patha);
//        _f.mkdirs();
//        createDefaultCvsXml(patha);
//        createDefaultTxtFile(patha, devicename);
//        patha = path + "/技术资料/生产资料/明细表/机加工件明细表/CVS";
//        _f = new File(patha);
//        _f.mkdirs();
//        createDefaultCvsXml(patha);
//        createDefaultTxtFile(patha, devicename);
//        patha = path + "/技术资料/生产资料/明细表/基本件明细表/CVS";
//        _f = new File(patha);
//        _f.mkdirs();
//        createDefaultCvsXml(patha);
//        createDefaultTxtFile(patha, devicename);
//        patha = path + "/技术资料/生产资料/明细表/外购件明细表/CVS";
//        _f = new File(patha);
//        _f.mkdirs();
//        createDefaultCvsXml(patha);
//        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/生产资料/装配工艺/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/技术资料/其他/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/设计资料/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/设计资料/参考标准/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/设计资料/同类产品/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/设计资料/元件选型/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/设计资料/其它/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/技术资料/宣传资料/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/宣传资料/宣传页/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/宣传资料/新闻稿/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/宣传资料/培训资料/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/技术资料/英文说明书/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/英文说明书/定制产品/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/英文说明书/历史版本/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/英文说明书/普通发货/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/中文说明书/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/中文说明书/定制产品/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/中文说明书/历史版本/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/技术资料/中文说明书/普通发货/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        //==========================================================
        patha = path + "/软件/CVS";
        _f = new File(patha);
        _f.mkdirs();
        cvssacllist = new ArrayList<CvsAcl>();
        ac = new CvsAcl();
        ac.set("deny");
        cvssacllist.add(ac);
        ac = new CvsAcl("root");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl("manager");
        ac.set("all");
        cvssacllist.add(ac);
        ac = new CvsAcl(devicename + "sourcecode");
        ac.set("read");
        ac.set("write");
        ac.set("tag");
        ac.set("create");
        cvssacllist.add(ac);
        createCvsXml(patha, cvssacllist);
//        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/软件/定制程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);

        patha = path + "/软件/历史程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/软件/开发文档/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/软件/其他/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/软件/英文程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/软件/中文程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);
        patha = path + "/软件/中英文合版程序/CVS";
        _f = new File(patha);
        _f.mkdirs();
        createDefaultCvsXml(patha);
        createDefaultTxtFile(patha, devicename);


    }

    private static void createDefaultTxtFile(String path, String devicename) {
        if (path != null) {
            path = path.trim();
        } else {
            return;
        }
        if (path.toLowerCase().endsWith("cvs")) {
            path = path.substring(0, path.length() - 4);
        }
        FilePlus.writeText(path + "/" + devicename + ".txt,v", "head\t1.1;\naccess;\nsymbols;\nlocks; strict;\ncomment\t@# @;\n\n\n1.1\ndate\t2010.12.13.03.59.30;\tauthor root;\tstate Exp;\nbranches;\nnext\t;\ndeltatype\ttext;\nkopt\tkv;\npermissions\t666;\ncommitid\tbcc4d059a22646c;\nfilename\t" + devicename + ".txt;\n\n\ndesc\n@@\n\n\n1.1\nlog\n@no message\n@\ntext\n@@\n");
    }

    private static void createDefaultCvsXml(String path) {
        if (!path.endsWith("/|\\")) {
            path += "/";
        }
        path = path + "fileattr.xml";
        FilePlus.writeText(path, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><fileattr><directory><owner>root</owner></directory></fileattr>");
    }

    private static void createCvsXml(String path, List<CvsAcl> cvssacllist) {
        if (!path.endsWith("/|\\")) {
            path += "/";
        }
        path = path + "fileattr.xml";
        try {
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("fileattr");
            Element dir = root.addElement("directory");
            //添加owner
            Element owner = dir.addElement("owner");
            owner.setText("root");
            for (Iterator<CvsAcl> it = cvssacllist.iterator(); it.hasNext();) {
                CvsAcl cvsAcl = it.next();
                Element ca = cvsAcl.getXmlElement();
                dir.add(ca);
            }
            XMLWriter writer = new XMLWriter(new FileWriter(path));
            writer.write(document);
            writer.close();
            //            System.out.println(doc.asXML());

        } catch (IOException ex) {
            Logger.getLogger(CvsTreeView.class.getName()).log(Level.SEVERE, null, ex);
        }
//        FilePlus.writeText(path + "/CVS/fileattr.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><fileattr><directory><owner>root</owner></directory></fileattr>");
    }
}
