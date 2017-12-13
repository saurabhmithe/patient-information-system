package projects.distributed.happypatients.springboot.model;

import java.util.Date;
import java.util.List;

public class Patient {
    private String id;
    private int age;
    private String name;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    private String bldGroup;
    private float height;
    private float weight;

    private List<TreatmentInformation> treatmentInformation;

    public String getBldGroup() {
        return bldGroup;
    }

    public void setBldGroup(String bldGroup) {
        this.bldGroup = bldGroup;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<TreatmentInformation> getTreatmentInformation() {
        return treatmentInformation;
    }

    public void setTreatments(List<TreatmentInformation> treatmentInformation) {
        this.treatmentInformation = treatmentInformation;
    }

}
