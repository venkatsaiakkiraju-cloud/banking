package com.securebank.loan.repository;
import com.securebank.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; import java.util.Optional;
@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Optional<Loan> findByLoanRef(String ref);
    List<Loan> findByCustomerId(Long customerId);
    List<Loan> findByStatus(Loan.LoanStatus status);
}
