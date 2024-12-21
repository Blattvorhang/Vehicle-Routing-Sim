package edu.tongji.vehicleroutingsim;

import edu.tongji.vehicleroutingsim.service.MapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VehicleRoutingSimApplicationTests {

    @Autowired
    private MapService mapService;

    /**
     * 测试 WebTestController
     */
    @Test
    void WebTestControllerTest() throws InterruptedException {
        //开启服务器
        SpringApplication.run(VehicleRoutingSimApplication.class);

        // 延迟 30 秒后关闭服务器
        Thread.sleep(30000);

    }

    /**
     * 测试 MapService的读取地图文件功能，并比较时间
     */
    @Test
    void FileReadTest() {
        // 测试读取地图文件时间
        mapService.loadMapFile();
        // 测试读取地图对象文件时间
        mapService.loadMapObject();
    }

}
