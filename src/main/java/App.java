import com.turing.tomcat.TomcatFactory;
import com.turing.tomcat.httpBIOImpl.BIOTomcat;
import com.turing.tomcat.httpInterface.Tomcat;
import com.turing.tomcat.httpNIOImpl.NIOResponse;
import com.turing.tomcat.httpNIOImpl.NIOTomcat;
import com.turing.tomcat.utils.PropertyMappingFactoryImpl;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.channels.Selector;

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
    }
}
