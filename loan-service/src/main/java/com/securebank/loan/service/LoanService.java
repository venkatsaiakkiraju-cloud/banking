package com.securebank.loan.service;
import com.securebank.loan.dto.LoanDto;
import com.securebank.loan.exception.LoanException;
import com.securebank.loan.model.Loan;
import com.securebank.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j @Service @RequiredArgsConstructor
public class LoanService {
    private final LoanRepository repo;

    private static final BigDecimal RATE_PERSONAL  = new BigDecimal("12.5");
    private static final BigDecimal RATE_HOME      = new BigDecimal("8.5");
    private static final BigDecimal RATE_CAR       = new BigDecimal("9.5");
    private static final BigDecimal RATE_EDUCATION = new BigDecimal("7.5");

    @Transactional
    public LoanDto.LoanResponse apply(LoanDto.ApplyRequest req) {
        String ref = "LOAN" + UUID.randomUUID().toString().substring(0,10).toUpperCase();
        BigDecimal rate = interestRateFor(req.getLoanType());
        BigDecimal emi = calculateEMI(req.getAmount(), rate, req.getTenureMonths());

        var loan = Loan.builder().loanRef(ref).customerId(req.getCustomerId()).loanType(req.getLoanType())
            .amount(req.getAmount()).interestRate(rate).tenureMonths(req.getTenureMonths())
            .emiAmount(emi).status(Loan.LoanStatus.PENDING).build();
        var saved = repo.save(loan);
        log.info("Loan applied: {} customer={} amount={}", ref, req.getCustomerId(), req.getAmount());
        return toResponse(saved);
    }

    @Transactional
    public LoanDto.LoanResponse approve(String ref, LoanDto.ApproveRequest req) {
        var loan = repo.findByLoanRef(ref).orElseThrow(()->new LoanException("Loan not found: "+ref));
        if(loan.getStatus()!=Loan.LoanStatus.PENDING) throw new LoanException("Only PENDING loans can be approved");
        loan.setStatus(Loan.LoanStatus.APPROVED);
        loan.setApprovedBy(req.getEmployeeId());
        log.info("Loan APPROVED: {} by employee={}", ref, req.getEmployeeId());
        return toResponse(repo.save(loan));
    }

    @Transactional
    public LoanDto.LoanResponse reject(String ref, LoanDto.RejectRequest req) {
        var loan = repo.findByLoanRef(ref).orElseThrow(()->new LoanException("Loan not found: "+ref));
        if(loan.getStatus()!=Loan.LoanStatus.PENDING) throw new LoanException("Only PENDING loans can be rejected");
        loan.setStatus(Loan.LoanStatus.REJECTED);
        loan.setRejectionReason(req.getReason());
        log.info("Loan REJECTED: {} reason={}", ref, req.getReason());
        return toResponse(repo.save(loan));
    }

    public LoanDto.LoanResponse getByRef(String ref) {
        return repo.findByLoanRef(ref).map(this::toResponse).orElseThrow(()->new LoanException("Loan not found: "+ref));
    }

    public List<LoanDto.LoanResponse> getByCustomer(Long customerId) {
        return repo.findByCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LoanDto.LoanResponse> getByStatus(Loan.LoanStatus status) {
        return repo.findByStatus(status).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<LoanDto.LoanResponse> getAllPending() { return getByStatus(Loan.LoanStatus.PENDING); }

    private BigDecimal interestRateFor(Loan.LoanType type) {
        return switch(type) {
            case PERSONAL -> RATE_PERSONAL;
            case HOME -> RATE_HOME;
            case CAR -> RATE_CAR;
            case EDUCATION -> RATE_EDUCATION;
        };
    }

    // Standard EMI formula: EMI = P * r * (1+r)^n / ((1+r)^n - 1)
    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("1200"), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(months);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
        if(denominator.compareTo(BigDecimal.ZERO)==0) return principal.divide(new BigDecimal(months), 2, RoundingMode.HALF_UP);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private LoanDto.LoanResponse toResponse(Loan l) {
        return LoanDto.LoanResponse.builder().id(l.getId()).loanRef(l.getLoanRef()).customerId(l.getCustomerId())
            .loanType(l.getLoanType()).amount(l.getAmount()).interestRate(l.getInterestRate()).tenureMonths(l.getTenureMonths())
            .emiAmount(l.getEmiAmount()).status(l.getStatus()).approvedBy(l.getApprovedBy())
            .rejectionReason(l.getRejectionReason()).createdAt(l.getCreatedAt()).build();
    }
}
