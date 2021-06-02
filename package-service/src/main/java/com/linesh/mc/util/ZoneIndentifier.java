package com.linesh.mc.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZoneIndentifier {

    @Value("#{'${north.zone.prefixes}'.split(',')}")
    private String[] northZonePrefixes;

    @Value("#{'${south.zone.prefixes}'.split(',')}")
    private String[] southZonePrefixes;

    @Value("#{'${east.zone.prefixes}'.split(',')}")
    private String[] eastZonePrefixes;

    @Value("#{'${west.zone.prefixes}'.split(',')}")
    private String[] westZonePrefixes;

    public boolean isNorthZoneZip(int zipCode) {
        return StringUtils.startsWithAny(String.valueOf(zipCode), northZonePrefixes);
    }

    public boolean isSouthZoneZip(int zipCode) {
        return StringUtils.startsWithAny(String.valueOf(zipCode), southZonePrefixes);
    }

    public boolean isEastZoneZip(int zipCode) {
        return StringUtils.startsWithAny(String.valueOf(zipCode), eastZonePrefixes);
    }

    public boolean isWestZoneZip(int zipCode) {
        return StringUtils.startsWithAny(String.valueOf(zipCode), westZonePrefixes);
    }
}
