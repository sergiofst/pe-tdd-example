package com.pe.tdd.service.impl;

import com.pe.tdd.domain.Account;
import com.pe.tdd.domain.SpeiTransfer;
import com.pe.tdd.exception.InsufficientBalanceException;
import com.pe.tdd.service.AccountActivityService;
import com.pe.tdd.service.AccountService;
import com.pe.tdd.service.SpeiTransferService;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SpeiTransferServiceImpl implements SpeiTransferService {

    AccountActivityService accountActivityService;

    AccountService accountService;

    public SpeiTransferServiceImpl(AccountActivityService accountActivityService, AccountService accountService) {
        this.accountActivityService = accountActivityService;
        this.accountService = accountService;
    }

    @Override
    public SpeiTransfer transfer(Account originAccount, String accountNumber, BigDecimal amount) {
        if (originAccount == null) {
            throw new IllegalArgumentException("originAccount");
        }
        if (StringUtils.isEmpty(accountNumber)) {
            throw new IllegalArgumentException("accountNumber");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a $0.00 Pesos");
        } else if (amount.compareTo(BigDecimal.valueOf(5000)) > 0) {
            throw new IllegalArgumentException("El monto no debe superar los $5000.00 Pesos");
        }

        if (originAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        SpeiTransfer result = new SpeiTransfer(
                "12345678",
                LocalDate.now(),
                amount
        );

        accountActivityService.addAccountActivity(result, originAccount);

        return result;
    }


}
