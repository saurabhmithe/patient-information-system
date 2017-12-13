package project.distributed.policyserver.service;

public interface PolicyService {
    public String getPolicy();

    public boolean updatePolicy(String policy);
}
