package com.linesh.mc.packaging;

import com.linesh.mc.packaging.api.PackageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PackageServiceApplicationTests {

    @Autowired
    PackageController packageController;

    @Test
    void contextLoads() {
        assertThat(packageController).isNotNull();
    }

}
