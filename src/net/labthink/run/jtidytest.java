/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.run;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.tidy.Tidy;

/**
 *
 * @author Moses
 */
public class jtidytest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        tidydochtml();
//        DOMParser parser = new DOMParser();
//        parser.setProperty(
//                "http://cyberneko.org/html/properties/default-encoding",
//                "gb18030");
    }

    public static void tidydochtml() {
        try {
            // TODO code application logic here
            Tidy tidy = new Tidy(); //使用Jtidy几乎只需要用的这一个类  
            //设置jtidy的配置文件，当然你也可以在程序根据需要中设置  
            tidy.setConfigurationFromFile("d:/temp/config.txt");
            tidy.setErrout(new PrintWriter("error.txt")); //输出错误与警告信息,默认输出到stdout  
            //需要转换的文件，当然你也可以转换URL的内容  
            FileInputStream in = new FileInputStream("F:/HELP（EC）en/info.htm");
            FileOutputStream out = new FileOutputStream("F:/HELP（EC）en/out.html");    //输出的文件  
//            BufferedInputStream bis= new BufferedInputStream(in);
//            String codec = getEncodingOfStream(bis);
//            System.out.println(codec);192.168
            tidy.setInputEncoding("utf-8");
            tidy.setOutputEncoding("utf-8");
//            tidy.setOutputEncoding("gb2312");
            tidy.parse(in, out);    //开始转换了~~~Jtidy把所有东西都封装好了，哈哈~~  
            out.close();    //转换完成关闭输入输出流  
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(jtidytest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getEncodingOfStream(BufferedInputStream bin) throws IOException {
        byte[] bytes = new byte[1024];  //存放读入的信息，一次读入1024个字节  
        bin.mark(1024); //标记初始位置,设标记失效的最大字节数为1024  
        int len = bin.read(bytes);
        String encoding;
        String encoding_tag = "<meta[^;]+;\\s*charset\\s*=\\s*([^\"\\s]+)[^>]*>";   //使用正则表达式匹配charset  
        String detector = new String(bytes, 0, len, "iso8859-1");   //默认用iso8859-1，避免丢失信息  
        Pattern encodingPattern = Pattern.compile(encoding_tag, Pattern.CASE_INSENSITIVE);
        Matcher m = encodingPattern.matcher(detector);
        if (m.find()) {
            encoding = m.group(1);  //第1个group匹配的就是需要的字符编码  
        } else {
            encoding = "iso8859-1"; //如果没找到就当做iso8859-1算了  
        }
        bin.reset();
        return encoding;
    }
}
