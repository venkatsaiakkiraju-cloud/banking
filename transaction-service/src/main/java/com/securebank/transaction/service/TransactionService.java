package com.securebank.transaction.service;
import com.securebank.transaction.config.ServiceUrlConfig;
import com.securebank.transaction.dto.TransactionDto;
import com.securebank.transaction.exception.TransactionException;
import com.securebank.transaction.model.Transaction;
import com.securebank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j @Service @RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repo;
    private final RestTemplate restTemplate;
    private final ServiceUrlConfig serviceUrlConfig;

    @Transactional
    public TransactionDto.TransactionResponse transfer(TransactionDto.TransferRequest req) {
        String ref = "TXN" + UUID.randomUUID().toString().substring(0,12).toUpperCase();

        if(req.getFromAccount().equals(req.getToAccount()))
            throw new TransactionException("Cannot transfer to the same account");

        try {
            // Debit from source account
            callAccountService(req.getFromAccount(), req.getAmount(), "DEBIT");
            // Credit to destination account
            callAccountService(req.getToAccount(), req.getAmount(), "CREDIT");

            var tx = Transaction.builder().transactionRef(ref).fromAccount(req.getFromAccount())
                .toAccount(req.getToAccount()).amount(req.getAmount()).transactionType(Transaction.TransactionType.TRANSFER)
                .status(Transaction.TransactionStatus.SUCCESS).description(req.getDescription()).build();
            var saved = repo.save(tx);
            log.info("Transfer SUCCESS: {} -> {} amount={} ref={}", req.getFromAccount(), req.getToAccount(), req.getAmount(), ref);
            return toResponse(saved);

        } catch(Exception e) {
            var tx = Transaction.builder().transactionRef(ref).fromAccount(req.getFromAccount())
                .toAccount(req.getToAccount()).amount(req.getAmount()).transactionType(Transaction.TransactionType.TRANSFER)
                .status(Transaction.TransactionStatus.FAILED).description("FAILED: "+e.getMessage()).build();
            repo.save(tx);
            log.error("Transfer FAILED: {} -> {} reason={}", req.getFromAccount(), req.getToAccount(), e.getMessage());
            throw new TransactionException("Transfer failed: " + e.getMessage());
        }
    }

    private void callAccountService(String accountNumber, BigDecimal amount, String operation) {
        Map<String,Object> body = new HashMap<>();
        body.put("amount", amount); body.put("operation", operation);
        String url = serviceUrlConfig.getAccountServiceUrl() + "/api/accounts/" + accountNumber + "/balance";
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body), String.class);
    }

    public List<TransactionDto.TransactionResponse> getHistory(String accountNumber) {
        return repo.findByFromAccountOrToAccountOrderByCreatedAtDesc(accountNumber, accountNumber)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TransactionDto.TransactionResponse> getMiniStatement(String accountNumber) {
        return repo.findByFromAccountOrToAccountOrderByCreatedAtDesc(accountNumber, accountNumber,
                org.springframework.data.domain.PageRequest.of(0,10))
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TransactionDto.TransactionResponse getByRef(String ref) {
        return repo.findByTransactionRef(ref).map(this::toResponse).orElseThrow(()->new TransactionException("Transaction not found: "+ref));
    }

    private TransactionDto.TransactionResponse toResponse(Transaction t) {
        return TransactionDto.TransactionResponse.builder().id(t.getId()).transactionRef(t.getTransactionRef())
            .fromAccount(t.getFromAccount()).toAccount(t.getToAccount()).amount(t.getAmount())
            .transactionType(t.getTransactionType()).status(t.getStatus()).description(t.getDescription()).createdAt(t.getCreatedAt()).build();
    }
}
