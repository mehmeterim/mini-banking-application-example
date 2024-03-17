package com.application.bank.DTO;

import java.math.BigDecimal;

public class AccountDTO {

    private String number;
    private String name;
    private BigDecimal balance;

    public AccountDTO() {
    }

    public AccountDTO(String number, String name, BigDecimal balance) {
        this.number = number;
        this.name = name;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
