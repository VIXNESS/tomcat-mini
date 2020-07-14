import com.turing.tomcat.httpBIOImpl.BIOTomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.channels.Selector;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args){
        new BIOTomcat(8087).start();
    }
}
