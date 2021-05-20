import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ResourceBundle;

public class Start {
    private final static String XML_PATH =  ResourceBundle.getBundle("application").getString("start");
    public static void start(){
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(XML_PATH);
        context.start();
    }
}
