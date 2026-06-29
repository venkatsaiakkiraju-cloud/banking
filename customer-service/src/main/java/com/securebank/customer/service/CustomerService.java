package com.securebank.customer.service;
import com.securebank.customer.dto.CustomerDto;
import com.securebank.customer.exception.CustomerException;
import com.securebank.customer.model.Customer;
import com.securebank.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List; import java.util.stream.Collectors;
@Slf4j @Service @RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repo;

    @Transactional
    public CustomerDto.CustomerResponse create(CustomerDto.CreateRequest req) {
        if(repo.existsByEmail(req.getEmail())) throw new CustomerException("Email already exists");
        var c = Customer.builder().userId(req.getUserId()).firstName(req.getFirstName()).lastName(req.getLastName())
            .email(req.getEmail()).phone(req.getPhone()).address(req.getAddress()).dateOfBirth(req.getDateOfBirth())
            .panNumber(req.getPanNumber()).aadharNumber(req.getAadharNumber()).kycStatus(Customer.KycStatus.PENDING).build();
        var saved=repo.save(c); log.info("Customer created: {}",saved.getEmail()); return toResponse(saved);
    }

    public CustomerDto.CustomerResponse getById(Long id) {
        return repo.findById(id).map(this::toResponse).orElseThrow(()->new CustomerException("Customer not found: "+id));
    }
    public CustomerDto.CustomerResponse getByUserId(Long uid) {
        return repo.findByUserId(uid).map(this::toResponse).orElseThrow(()->new CustomerException("Customer not found for user: "+uid));
    }
    public List<CustomerDto.CustomerResponse> getAll() { return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList()); }

    @Transactional
    public CustomerDto.CustomerResponse update(Long id, CustomerDto.UpdateRequest req) {
        var c=repo.findById(id).orElseThrow(()->new CustomerException("Customer not found"));
        if(req.getFirstName()!=null) c.setFirstName(req.getFirstName());
        if(req.getLastName()!=null)  c.setLastName(req.getLastName());
        if(req.getPhone()!=null)     c.setPhone(req.getPhone());
        if(req.getAddress()!=null)   c.setAddress(req.getAddress());
        return toResponse(repo.save(c));
    }

    @Transactional
    public CustomerDto.CustomerResponse updateKyc(Long id, Customer.KycStatus status) {
        var c=repo.findById(id).orElseThrow(()->new CustomerException("Customer not found"));
        c.setKycStatus(status); log.info("KYC {} -> {}",id,status); return toResponse(repo.save(c));
    }

    private CustomerDto.CustomerResponse toResponse(Customer c) {
        return CustomerDto.CustomerResponse.builder().id(c.getId()).userId(c.getUserId()).firstName(c.getFirstName())
            .lastName(c.getLastName()).email(c.getEmail()).phone(c.getPhone()).address(c.getAddress())
            .dateOfBirth(c.getDateOfBirth()).panNumber(c.getPanNumber()).kycStatus(c.getKycStatus()).createdAt(c.getCreatedAt()).build();
    }
}
