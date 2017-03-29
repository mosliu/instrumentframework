package net.labthink.run;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MaintTest {
    /** The logger. */
    static Logger logger = Logger.getLogger(MaintTest.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
	new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    // public void initializeViaSpring() throws Exception {
    // new ClassPathXmlApplicationContext("trapReceiverContext.xml");
    // }

    /**
     * 加载Log4J模块.
     */
    public static void loadLog4j() {
	PropertyConfigurator.configure("./bin/conf/log4j.properties");
	// DOMConfigurator.configure("E:/study/log4j/log4j.xml"); //加载.xml文件
	// File f = new File("./");
	// System.out.println(f.getAbsolutePath());
	logger.info("加载Log4J");
    }

    /** Spring Bean Factory. */
    private static XmlBeanFactory beanFactory;

    /**
     * Load spring bean factory. 加载Spring配置
     */
    public static void loadSpringBeanFactory() {
	// InputStream is = new
	// FileInputStream("E:\\proj\\applicationContext.xml");
	// Resource resource = new InputStreamResource(is);
	// XmlBeanFactory bean = new XmlBeanFactory(resource);
	try {
	    Resource resource = new ClassPathResource(
		    "./conf/applicationContext.xml");
	    beanFactory = new XmlBeanFactory(resource);
	} catch (Exception e) {
	    logger.error("Spring配置文件加载失败！" + e.getMessage());
	}
    }

    /**
     * Gets the bean factory.
     * 
     * @return the bean factory
     */
    public static XmlBeanFactory getBeanFactory() {
	if (beanFactory == null) {
	    loadSpringBeanFactory();
	}
	return beanFactory;
    }
}

