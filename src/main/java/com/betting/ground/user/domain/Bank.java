package com.betting.ground.user.domain;

import com.betting.ground.user.dto.UserAccountDto;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Bank {
    private String bankName;
    private String bankAccount;

    public Bank(UserAccountDto userAccountDTO) {
        this.bankName = userAccountDTO.getBankName();
        this.bankAccount = userAccountDTO.getBankAccount();
    }
}
