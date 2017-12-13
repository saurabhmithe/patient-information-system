package projects.distributed.happypatients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projects.distributed.happypatients.springboot.cache.CacheLoader;
import projects.distributed.happypatients.springboot.utilities.CassandraConnector;
import projects.distributed.happypatients.springboot.utilities.Producer;

@SpringBootApplication
public class HappypatientsApplication {

    public static Producer producer;

    @Value("${cassandra.server.url}")
    private static String serverUrl = "54.219.189.10";
    @Value("${cassandra.server.port}")
    private static Integer serverPort = 9042;

    public static void main(String[] args) {
        CassandraConnector cc = new CassandraConnector();
        producer = new Producer();

        try {
            cc.connect(serverUrl, serverPort);
            producer.create("HappyPatientsHospital");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(HappypatientsApplication.class, args);
    }
}
