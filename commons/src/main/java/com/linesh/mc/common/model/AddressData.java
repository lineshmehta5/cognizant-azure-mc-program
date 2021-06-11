package com.linesh.mc.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.linesh.mc.common.enums.Zone;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressData implements Serializable {
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private int zipcode;
    private String state;
    private Zone zone = null;
}
