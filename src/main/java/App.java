
import com.turing.labs.CyclicBarrierLab;
import com.turing.tomcat.httpAIOImpl.AIOTomcat;
import com.turing.tomcat.httpInterface.Tomcat;
import com.turing.tomcat.httpNIOImpl.NIOTomcat;
import com.turing.tomcat.utils.PropertyMappingFactoryImpl;
import com.turing.tomcat.utils.PropertyMappingFactoryInf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CyclicBarrier;


public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(".");
            }

        }).start();
//        try (){
//            tomcat.start();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
        AIOTomcat tomcat = new AIOTomcat(new PropertyMappingFactoryImpl(PropertyMappingFactoryInf.MapType.Enum), 9001);
        tomcat.start();
    }
}
