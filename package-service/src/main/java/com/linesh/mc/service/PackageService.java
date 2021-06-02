package com.linesh.mc.service;

import com.linesh.mc.enums.Zone;
import com.linesh.mc.jms.CourierServiceDelegate;
import com.linesh.mc.model.ItemData;
import com.linesh.mc.model.OrderData;
import com.linesh.mc.util.ZoneIndentifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PackageService {

    @Autowired
    ZoneIndentifier zoneIndentifier;

    @Autowired
    CourierServiceDelegate courierServiceDelegate;

    public void sortAndSendPackages(OrderData orderData) {
        log.info("Processing OrderData");
        List<ItemData> eastZoneItemData = orderData.getItemDataList().stream()
                .filter(itemData -> zoneIndentifier.isEastZoneZip(itemData.getAddressData().getZipcode()))
                .collect(Collectors.toList());
        List<ItemData> westZoneItemData = orderData.getItemDataList().stream()
                .filter(itemData -> zoneIndentifier.isWestZoneZip(itemData.getAddressData().getZipcode()))
                .collect(Collectors.toList());
        List<ItemData> southZoneItemData = orderData.getItemDataList().stream()
                .filter(itemData -> zoneIndentifier.isSouthZoneZip(itemData.getAddressData().getZipcode()))
                .collect(Collectors.toList());
        List<ItemData> northZoneItemData = orderData.getItemDataList().stream()
                .filter(itemData -> zoneIndentifier.isNorthZoneZip(itemData.getAddressData().getZipcode()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(eastZoneItemData)) {
            eastZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.EAST));
            sendToCourierService(eastZoneItemData);
        }
        if (CollectionUtils.isNotEmpty(westZoneItemData)) {
            westZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.WEST));
            sendToCourierService(westZoneItemData);
        }
        if (CollectionUtils.isNotEmpty(southZoneItemData)) {
            southZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.SOUTH));
            sendToCourierService(southZoneItemData);
        }
        if (CollectionUtils.isNotEmpty(northZoneItemData)) {
            northZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.NORTH));
            sendToCourierService(northZoneItemData);
        }
    }

    private void sendToCourierService(List<ItemData> itemDataList) {
        courierServiceDelegate.postMessageToQueue(itemDataList);
    }
}
