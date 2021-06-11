package com.linesh.mc.packaging.service;

import com.linesh.mc.common.enums.Zone;
import com.linesh.mc.common.model.ItemData;
import com.linesh.mc.common.model.OrderData;
import com.linesh.mc.common.model.ShippingUnit;
import com.linesh.mc.packaging.jms.ShippingServiceDelegate;
import com.linesh.mc.packaging.util.ZoneIndentifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PackageService {

    @Autowired
    ZoneIndentifier zoneIndentifier;

    @Autowired
    ShippingServiceDelegate shippingServiceDelegate;

    private static final String LOG_MESSAGE = "Sending {} zone package for shipping!";

    public void sortAndSendPackages(OrderData orderData) {
        log.info("Processing OrderData with Order Id {}", orderData.getOrderId());
        orderData.getItemDataList().forEach(itemData -> itemData.setOrderId(orderData.getOrderId()));
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
            log.info(LOG_MESSAGE, Zone.EAST);
            sendToShippingService(new ShippingUnit(UUID.randomUUID().toString(), orderData.getOrderId(), eastZoneItemData));
        }
        if (CollectionUtils.isNotEmpty(westZoneItemData)) {
            westZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.WEST));
            log.info(LOG_MESSAGE, Zone.WEST);
            sendToShippingService(new ShippingUnit(UUID.randomUUID().toString(), orderData.getOrderId(), westZoneItemData));
        }
        if (CollectionUtils.isNotEmpty(southZoneItemData)) {
            southZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.SOUTH));
            log.info(LOG_MESSAGE, Zone.SOUTH);
            sendToShippingService(new ShippingUnit(UUID.randomUUID().toString(), orderData.getOrderId(), southZoneItemData));
        }
        if (CollectionUtils.isNotEmpty(northZoneItemData)) {
            northZoneItemData.parallelStream().forEach(itemData -> itemData.getAddressData().setZone(Zone.NORTH));
            log.info(LOG_MESSAGE, Zone.NORTH);
            sendToShippingService(new ShippingUnit(UUID.randomUUID().toString(), orderData.getOrderId(), northZoneItemData));
        }
    }

    private void sendToShippingService(ShippingUnit shippingUnit) {
        shippingServiceDelegate.postMessageToQueue(shippingUnit);
    }
}
