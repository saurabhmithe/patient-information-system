package project.distributed.policyserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import project.distributed.policyserver.service.PolicyService;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    public static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    @Autowired
    PolicyService policyService;

    // -------------------Retrieve Policy---------------------------------------------

    @RequestMapping(value = "/retrieve/", method = RequestMethod.GET)
    public ResponseEntity<?> getPolicy() {
        String policy = policyService.getPolicy();
        if (policy == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<String>(policy, HttpStatus.OK);
    }

    // -------------------Provision Policy---------------------------------------------

    @RequestMapping(value = "/provision/{policy}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePolicy(@PathVariable("policy") String policy) {
        if (!policyService.updatePolicy(policy)) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
