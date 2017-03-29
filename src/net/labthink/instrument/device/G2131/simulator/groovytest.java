
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package net.labthink.instrument.device.G2131.simulator;

//~--- non-JDK imports --------------------------------------------------------

//import groovy.lang.Binding;
//import groovy.lang.GroovyClassLoader;
//import groovy.lang.GroovyObject;
//import groovy.lang.GroovyShell;
//import groovy.lang.Script;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Moses
 */
public class groovytest {
    public static void main(String[] args) {
//	long   starttime = System.currentTimeMillis();
//	String src       =
//	    "def run(time) {\r\n"
//	    + "result = Math.abs(2e-10*Math.pow(time, 4)-1e-06*Math.pow(time, 3)+0.00278*Math.pow(time, 2)-0.34281*time-0.59106)"
//	    + "\r\n}" + "\r\nrun time";
//	ClassLoader       parent = new groovytest().getClass().getClassLoader();
//	GroovyClassLoader loader = new GroovyClassLoader(parent);
//
//	try {
//	    Class        groovyClass  = loader.parseClass(src);
//	    GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
//	    Object[]     params       = { 20 };
//
//	    System.out.println(groovyObject.invokeMethod("run", params));
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//
//	long endtime = System.currentTimeMillis();
//
//	System.out.println("耗时：" + (endtime - starttime));
//    }
//
//    public static void main3(String[] args) {
//	ScriptEngineManager factory = new ScriptEngineManager();
//	ScriptEngine        engine  = factory.getEngineByName("JavaScript");
//
//	try {
//	    engine.eval("print(2e-10*24238956971265)");
//	} catch (ScriptException ex) {
//	    Logger.getLogger(groovytest.class.getName()).log(Level.SEVERE, null, ex);
//	}
//    }
//
//    public static void main2(String[] args) {
//	long   starttime = System.currentTimeMillis();
//	String src       =
//	    "result = Math.abs(2e-10*Math.pow(time, 4)-1e-06*Math.pow(time, 3)+0.00278*Math.pow(time, 2)-0.34281*time-0.59106)";
//	Binding binding = new Binding();
//
//	binding.setVariable("time", 10);
//
//	GroovyShell shell  = new GroovyShell(binding);
//	Object      b      = shell.evaluate(src);    // 此句为关键代码，利用java执行了code.txt中的动态计算公式的代码
//	Object      result = (Object) binding.getVariable("result");
//
//	System.out.println("result:" + result);
//
//	long endtime = System.currentTimeMillis();
//
//	System.out.println("耗时：" + (endtime - starttime));
//	binding.setVariable("time", 40);
//
//	Script sc = shell.parse(src);
//
//	b      = shell.evaluate(src);
//	result = (Object) binding.getVariable("result");
//	System.out.println("result:" + result);
//	endtime = System.currentTimeMillis();
//	System.out.println("耗时：" + (endtime - starttime));
    }
}



