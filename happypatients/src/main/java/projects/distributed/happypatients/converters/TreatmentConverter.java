package projects.distributed.happypatients.converters;

import com.datastax.driver.core.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import projects.distributed.happypatients.springboot.model.TreatmentInformation;
import projects.distributed.happypatients.springboot.model.TreatmentStatus;

@Component
public class TreatmentConverter implements Converter<Row, TreatmentInformation> {

    @Override
    public TreatmentInformation convert(Row row) {
        TreatmentInformation treatmentInformation = new TreatmentInformation();
        treatmentInformation.setPatientId(row.getUUID("pid").toString());
        treatmentInformation.setDiagnosis(row.getString("diagnosis"));
        treatmentInformation.setMedicalCondition(row.getString("medicalcondition"));
        treatmentInformation.setDoctorName(row.getString("doctor"));
        treatmentInformation.setEndDate(row.getTimestamp("enddate"));
        treatmentInformation.setReport(row.getString("report"));
        treatmentInformation.setStartDate(row.getTimestamp("startdate"));
        treatmentInformation.setTreatmentStatus(getTreatmentStatusEnum(row.getString("status")));
        return treatmentInformation;
    }

    private TreatmentStatus getTreatmentStatusEnum(String string) {
        for (TreatmentStatus t : TreatmentStatus.values()) {
            if (t.toString().equalsIgnoreCase(string))
                return t;
        }
        return TreatmentStatus.NORMAL;
    }


}
