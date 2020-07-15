
import com.turing.tomcat.httpNIOImpl.NIOTomcat;
import com.turing.tomcat.utils.PropertyMappingFactoryImpl;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;


public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args){
//        try {
//            Tomcat tomcat = TomcatFactory.getTomcat(NIOTomcat.class, new PropertyMappingFactoryImpl(PropertyMappingFactoryInf.MapType.Enum), 8080);
//            tomcat.start();
//        }catch (Exception e){
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
        new NIOTomcat(new PropertyMappingFactoryImpl(PropertyMappingFactoryInf.MapType.Enum),8086).start();

//        App app = new App();
//        try(InputStream inputStream = app.getClass().getResourceAsStream("web.properties")){
//            logger.info("starting");
//            Properties properties = new Properties();
//            properties.load(inputStream);
//            properties.keySet().stream().forEach(System.out::print);
//        }catch (Exception e){
//            e.printStackTrace();
//        }


    }
}
