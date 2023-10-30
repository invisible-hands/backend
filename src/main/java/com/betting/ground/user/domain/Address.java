package com.betting.ground.user.domain;

import com.betting.ground.user.dto.UserAddressDTO;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {
    private String roadName;
    private String addressName;
    private Integer zipcode;
    private String detailAddress;

    public Address(UserAddressDTO dto){
        this.roadName = dto.getRoadName();
        this.addressName = dto.getAddressName();
        this.zipcode = dto.getZipcode();
        this.detailAddress = dto.getDetailAddress();
    }
}