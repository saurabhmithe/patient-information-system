package projects.distributed.happypatients.springboot.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import projects.distributed.happypatients.springboot.model.Patient;

import java.io.IOException;

@Component
public class JsonConverter {
    public Patient getPatientObjectFromJson(String patient) {
        ObjectMapper mapper = new ObjectMapper();

        //JSON from String to Object
        try {
            Patient patientObject = mapper.readValue(patient, Patient.class);
            return patientObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJsonFromPatientObject(Patient patient) {
        ObjectMapper mapper = new ObjectMapper();

        //Object to JSON in String
        try {
            String jsonInString = mapper.writeValueAsString(patient);
            return jsonInString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}