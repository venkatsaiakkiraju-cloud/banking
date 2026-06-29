package com.securebank.transaction.repository;
import com.securebank.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List; import java.util.Optional;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByTransactionRef(String ref);
    List<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(String fromAcc, String toAcc, Pageable pageable);
    List<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(String fromAcc, String toAcc);
}
