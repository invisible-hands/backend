package com.betting.ground.user.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String roadName;
    private String addressName;
    private int zipcode;
    private String detailAddress;
}
