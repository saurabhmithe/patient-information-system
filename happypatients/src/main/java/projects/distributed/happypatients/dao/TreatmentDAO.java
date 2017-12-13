package projects.distributed.happypatients.dao;

import com.datastax.driver.core.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class TreatmentDAO extends GenericDAO {

    static final Logger LOG = LoggerFactory.getLogger(TreatmentDAO.class);

    public TreatmentDAO() {
        super();
    }

    public ResultSet getPatientsTreatment(UUID id) {
        ResultSet results = session.execute("SELECT * FROM treatment where pid=" + id);
        return results;
    }

    public ResultSet getPatientsTreatment(UUID id, String medicalCondition) {
        ResultSet results = session.execute("SELECT * FROM treatment where pid=" + id + " and medicalcondition='" + medicalCondition + "'");
        return results;
    }

    public boolean createTreatment(UUID id, String medicalCond, String diagnosis, String doctor, Date enddate, String report, Date startdate, String status) {
        try {
            session.execute("INSERT INTO treatment "
                    + "(pid, "
                    + "medicalcondition,"
                    + "diagnosis, "
                    + "doctor,"
                    + "enddate, "
                    + "report, "
                    + "startdate, "
                    + "status) VALUES ("
                    + id + ", '" + medicalCond + "', '" + diagnosis + "', '" + doctor + "'," + enddate.getTime() + ", '" + report + "'," + startdate.getTime() + ",'" + status + "')");

            return true;
        } catch (Exception e) {
            LOG.info("Create patient error " + e.getMessage());
            return false;
        }
    }

    public boolean updatePatient(UUID id, String medicalCond, String diagnosis, String doctor, Date enddate, String report, Date startdate, String status) {
        try {

            session.execute("UPDATE treatment set diagnosis='" + diagnosis + "', doctor='" + doctor + "',"
                    + "enddate=" + enddate.getTime() + ",report='" + report + "',startdate=" + startdate.getTime() + ",status='" + status + "' where pid=" + id + " and medicalcondition='" + medicalCond + "'");

            return true;
        } catch (Exception e) {
            LOG.error("Update patient error " + e.getMessage());
            return false;
        }
    }

    public boolean deletePatientTreatment(UUID id, String medicalCond) {
        try {

            session.execute("DELETE from treatment WHERE pid=" + id + "and medicalcondition='" + medicalCond + "'");

            return true;
        } catch (Exception e) {
            LOG.error("Delete patient error " + e.getMessage());
            return false;
        }
    }

    public boolean deletePatientTreatment(UUID id) {
        try {

            session.execute("DELETE from treatment WHERE pid=" + id);

            return true;
        } catch (Exception e) {
            LOG.error("Delete patient error " + e.getMessage());
            return false;
        }
    }

}
