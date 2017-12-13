package projects.distributed.happypatients.dao;

import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projects.distributed.happypatients.springboot.utilities.CassandraConnector;

public class GenericDAO {

    static final Logger LOG = LoggerFactory.getLogger(GenericDAO.class);
    static Session session;

    public GenericDAO() {
        session = CassandraConnector.getSession();
    }

}
