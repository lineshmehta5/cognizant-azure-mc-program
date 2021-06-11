package com.linesh.mc.packaging;

import com.linesh.mc.common.enums.Zone;
import com.linesh.mc.common.model.AddressData;
import com.linesh.mc.common.model.ItemData;
import com.linesh.mc.common.model.OrderData;
import com.linesh.mc.packaging.jms.ShippingServiceDelegate;
import com.linesh.mc.packaging.service.PackageService;
import com.linesh.mc.packaging.util.ZoneIndentifier;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class PackageServiceTest {

    @InjectMocks
    PackageService packageService;

    @Mock
    ShippingServiceDelegate shippingServiceDelegate;

    @Mock
    ZoneIndentifier zoneIndentifier;

    @Test
    void sortAndSendPackagesNorthZone() throws Exception {
        doNothing().when(shippingServiceDelegate).postMessageToQueue(Mockito.any());
        when(zoneIndentifier.isNorthZoneZip(Mockito.anyInt())).thenCallRealMethod();
        ReflectionTestUtils.setField(zoneIndentifier, "northZonePrefixes", new String[]{"0", "1", "2"});
        packageService.sortAndSendPackages(getOrderData(Zone.NORTH));
        verify(shippingServiceDelegate, times(1)).postMessageToQueue(Mockito.any());
    }

    @Test
    void sortAndSendPackagesEastZone() throws Exception {
        doNothing().when(shippingServiceDelegate).postMessageToQueue(Mockito.any());
        when(zoneIndentifier.isEastZoneZip(Mockito.anyInt())).thenCallRealMethod();
        ReflectionTestUtils.setField(zoneIndentifier, "eastZonePrefixes", new String[]{"3", "4"});
        packageService.sortAndSendPackages(getOrderData(Zone.EAST));
        verify(shippingServiceDelegate, times(1)).postMessageToQueue(Mockito.any());
    }

    @Test
    void sortAndSendPackagesSouthZone() throws Exception {
        doNothing().when(shippingServiceDelegate).postMessageToQueue(Mockito.any());
        when(zoneIndentifier.isSouthZoneZip(Mockito.anyInt())).thenCallRealMethod();
        ReflectionTestUtils.setField(zoneIndentifier, "southZonePrefixes", new String[]{"5", "6"});
        packageService.sortAndSendPackages(getOrderData(Zone.SOUTH));
        verify(shippingServiceDelegate, times(1)).postMessageToQueue(Mockito.any());
    }

    @Test
    void sortAndSendPackagesWestZone() throws Exception {
        doNothing().when(shippingServiceDelegate).postMessageToQueue(Mockito.any());
        when(zoneIndentifier.isWestZoneZip(Mockito.anyInt())).thenCallRealMethod();
        ReflectionTestUtils.setField(zoneIndentifier, "westZonePrefixes", new String[]{"7", "8", "9"});
        packageService.sortAndSendPackages(getOrderData(Zone.WEST));
        verify(shippingServiceDelegate, times(1)).postMessageToQueue(Mockito.any());
    }


    private OrderData getOrderData(Zone zone) {
        OrderData orderData = new OrderData();
        List<ItemData> itemDataList = new ArrayList<>();
        ItemData itemData1 = new ItemData();
        ItemData itemData2 = new ItemData();
        ItemData itemData3 = new ItemData();
        AddressData addressData1 = new AddressData();
        AddressData addressData2 = new AddressData();
        AddressData addressData3 = new AddressData();
        switch (zone) {
            case EAST:
                addressData1.setZipcode(3);
                addressData2.setZipcode(4);
                addressData3.setZipcode(3);
                break;
            case NORTH:
                addressData1.setZipcode(0);
                addressData2.setZipcode(1);
                addressData3.setZipcode(2);
                break;
            case SOUTH:
                addressData1.setZipcode(5);
                addressData2.setZipcode(6);
                addressData3.setZipcode(6);
                break;
            case WEST:
                addressData1.setZipcode(7);
                addressData2.setZipcode(8);
                addressData3.setZipcode(9);
        }
        itemData1.setAddressData(addressData1);
        itemData2.setAddressData(addressData2);
        itemData3.setAddressData(addressData3);
        itemDataList.add(itemData1);
        itemDataList.add(itemData2);
        itemDataList.add(itemData3);
        orderData.setItemDataList(itemDataList);
        return orderData;
    }
}
