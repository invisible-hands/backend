package com.betting.ground.user.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Bank {
    private String bankName;
    private String bankAccount;
}
