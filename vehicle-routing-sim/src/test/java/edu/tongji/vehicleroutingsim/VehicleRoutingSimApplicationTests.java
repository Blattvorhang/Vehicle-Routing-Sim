package edu.tongji.vehicleroutingsim;

import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.MapService;
import edu.tongji.vehicleroutingsim.service.PassengerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VehicleRoutingSimApplicationTests {

    @Autowired
    private MapService mapService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private CarService carService;

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
        mapService.saveMapObject();
        // 测试读取地图对象文件时间
        mapService.loadMapObject();
    }

    @Test
    void RandomPassengerTest() {
        mapService.loadMapObject();
        // 测试随机生成乘客
        passengerService.randomPassenger(3);
    }

    @Test
    void RadomCarTest() {
        mapService.loadMapObject();
        carService.randomCar(5);
    }


}
