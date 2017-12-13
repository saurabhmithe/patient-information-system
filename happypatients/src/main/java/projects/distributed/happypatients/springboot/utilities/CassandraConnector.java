package projects.distributed.happypatients.springboot.utilities;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class CassandraConnector {

    private static Cluster cluster;

    private static Session session;

    public static Session getSession() {
        return session;
    }

    public void connect(String node, Integer port) {
        Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        session = cluster.connect();

        session.execute("USE hphospital");
    }

    public void close() {
        session.close();
        cluster.close();
    }

}
