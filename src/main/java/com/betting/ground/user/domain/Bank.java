package com.betting.ground.user.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Bank {
    private String bankName;
    private String bankAccount;
}
