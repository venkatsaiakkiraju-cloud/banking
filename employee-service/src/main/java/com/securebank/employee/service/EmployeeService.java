package com.securebank.employee.service;
import com.securebank.employee.config.ServiceUrlConfig;
import com.securebank.employee.dto.EmployeeDto;
import com.securebank.employee.exception.EmployeeException;
import com.securebank.employee.model.Employee;
import com.securebank.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j @Service @RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repo;
    private final RestTemplate restTemplate;
    private final ServiceUrlConfig urls;

    @Transactional
    public EmployeeDto.EmployeeResponse create(EmployeeDto.CreateRequest req) {
        String code = "EMP" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        var emp = Employee.builder().userId(req.getUserId()).employeeCode(code).firstName(req.getFirstName())
            .lastName(req.getLastName()).email(req.getEmail()).department(req.getDepartment()).designation(req.getDesignation()).build();
        var saved = repo.save(emp);
        log.info("Employee created: {}", code);
        return toResponse(saved);
    }

    public EmployeeDto.EmployeeResponse getByUserId(Long userId) {
        return repo.findByUserId(userId).map(this::toResponse).orElseThrow(()->new EmployeeException("Employee not found"));
    }

    // Calls customer-service to update KYC status
    public Map<String,Object> verifyCustomer(EmployeeDto.VerifyCustomerRequest req) {
        String url = urls.getCustomerServiceUrl() + "/api/customers/" + req.getCustomerId() + "/kyc?status=" + req.getStatus();
        Map<String,Object> result = restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, null, Map.class).getBody();
        log.info("Customer {} verification set to {} by employee {}", req.getCustomerId(), req.getStatus(), req.getEmployeeId());
        return result;
    }

    // Calls account-service to freeze an account
    public Map<String,Object> freezeAccount(EmployeeDto.AccountActionRequest req) {
        String url = urls.getAccountServiceUrl() + "/api/accounts/" + req.getAccountNumber() + "/freeze";
        Map<String,Object> result = restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, null, Map.class).getBody();
        log.warn("Account {} FROZEN by employee {} reason={}", req.getAccountNumber(), req.getEmployeeId(), req.getReason());
        return result;
    }

    public Map<String,Object> unfreezeAccount(EmployeeDto.AccountActionRequest req) {
        String url = urls.getAccountServiceUrl() + "/api/accounts/" + req.getAccountNumber() + "/unfreeze";
        Map<String,Object> result = restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, null, Map.class).getBody();
        log.info("Account {} UNFROZEN by employee {}", req.getAccountNumber(), req.getEmployeeId());
        return result;
    }

    private EmployeeDto.EmployeeResponse toResponse(Employee e) {
        return EmployeeDto.EmployeeResponse.builder().id(e.getId()).userId(e.getUserId()).employeeCode(e.getEmployeeCode())
            .firstName(e.getFirstName()).lastName(e.getLastName()).email(e.getEmail())
            .department(e.getDepartment()).designation(e.getDesignation()).createdAt(e.getCreatedAt()).build();
    }
}
