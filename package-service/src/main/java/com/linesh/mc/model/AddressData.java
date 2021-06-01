package com.linesh.mc.model;

import lombok.Data;

@Data
public class AddressData {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private int zipcode;
    private String state;
}
