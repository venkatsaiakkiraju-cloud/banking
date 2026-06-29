package com.securebank.account.service;
import com.securebank.account.dto.AccountDto;
import com.securebank.account.exception.AccountException;
import com.securebank.account.model.Account;
import com.securebank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j @Service @RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repo;

    @Transactional
    public AccountDto.AccountResponse create(AccountDto.CreateRequest req) {
        String accNo = generateAccountNumber();
        var acc = Account.builder().accountNumber(accNo).customerId(req.getCustomerId())
            .accountType(req.getAccountType()).balance(req.getInitialDeposit()!=null?req.getInitialDeposit():BigDecimal.ZERO)
            .status(Account.AccountStatus.ACTIVE).build();
        var saved = repo.save(acc);
        log.info("Account created: {} for customer {}", accNo, req.getCustomerId());
        return toResponse(saved);
    }

    public AccountDto.AccountResponse getByAccountNumber(String accNo) {
        return repo.findByAccountNumber(accNo).map(this::toResponse).orElseThrow(()->new AccountException("Account not found: "+accNo));
    }

    public List<AccountDto.AccountResponse> getByCustomerId(Long customerId) {
        return repo.findByCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AccountDto.BalanceResponse getBalance(String accNo) {
        var acc = repo.findByAccountNumber(accNo).orElseThrow(()->new AccountException("Account not found: "+accNo));
        return AccountDto.BalanceResponse.builder().accountNumber(acc.getAccountNumber()).balance(acc.getBalance()).status(acc.getStatus()).build();
    }

    @Transactional
    public AccountDto.BalanceResponse updateBalance(String accNo, AccountDto.BalanceUpdateRequest req) {
        var acc = repo.findByAccountNumberForUpdate(accNo).orElseThrow(()->new AccountException("Account not found: "+accNo));
        if(acc.getStatus()==Account.AccountStatus.FROZEN) throw new AccountException("Account is frozen. Operation not allowed.");
        if(acc.getStatus()==Account.AccountStatus.CLOSED) throw new AccountException("Account is closed.");

        if("DEBIT".equalsIgnoreCase(req.getOperation())) {
            if(acc.getBalance().compareTo(req.getAmount())<0) throw new AccountException("Insufficient balance");
            acc.setBalance(acc.getBalance().subtract(req.getAmount()));
        } else if("CREDIT".equalsIgnoreCase(req.getOperation())) {
            acc.setBalance(acc.getBalance().add(req.getAmount()));
        } else {
            throw new AccountException("Invalid operation. Use CREDIT or DEBIT");
        }
        var saved = repo.save(acc);
        log.info("Balance {} on {}: amount={}", req.getOperation(), accNo, req.getAmount());
        return AccountDto.BalanceResponse.builder().accountNumber(saved.getAccountNumber()).balance(saved.getBalance()).status(saved.getStatus()).build();
    }

    @Transactional
    public AccountDto.AccountResponse freezeAccount(String accNo) {
        var acc = repo.findByAccountNumber(accNo).orElseThrow(()->new AccountException("Account not found"));
        acc.setStatus(Account.AccountStatus.FROZEN);
        log.warn("Account FROZEN: {}", accNo);
        return toResponse(repo.save(acc));
    }

    @Transactional
    public AccountDto.AccountResponse unfreezeAccount(String accNo) {
        var acc = repo.findByAccountNumber(accNo).orElseThrow(()->new AccountException("Account not found"));
        acc.setStatus(Account.AccountStatus.ACTIVE);
        log.info("Account UNFROZEN: {}", accNo);
        return toResponse(repo.save(acc));
    }

    private String generateAccountNumber() {
        long rand = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        return "SBK" + rand;
    }

    private AccountDto.AccountResponse toResponse(Account a) {
        return AccountDto.AccountResponse.builder().id(a.getId()).accountNumber(a.getAccountNumber()).customerId(a.getCustomerId())
            .accountType(a.getAccountType()).balance(a.getBalance()).status(a.getStatus()).ifscCode(a.getIfscCode())
            .branchName(a.getBranchName()).createdAt(a.getCreatedAt()).build();
    }
}
