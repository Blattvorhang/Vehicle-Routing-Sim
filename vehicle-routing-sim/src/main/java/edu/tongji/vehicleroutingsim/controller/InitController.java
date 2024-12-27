package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.model.DidiCar;
import edu.tongji.vehicleroutingsim.model.DidiPassenger;
import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p>
 * 初始化控制器
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 13:47
 */
@RestController
public class InitController {

    private static final Logger logger = LoggerFactory.getLogger(InitController.class);
    private final CarService carService;
    private final PassengerService passengerService;
    private final int carNums;
    private final int passengerNums;

    @Autowired
    public InitController(@Value("${car.nums}") int carNums,
                          @Value("${passenger.nums}") int passengerNums, CarService carService, PassengerService passengerService) {
        this.carNums = carNums;
        this.passengerNums = passengerNums;
        this.carService = carService;
        this.passengerService = passengerService;
    }

    /**
     * 获取五辆小车的初始位置和三个乘客的初始位置与目的地
     *
     * @return 包含小车和乘客信息的 Map
     */
    @GetMapping("/api/init")
    public Map<String, Object> getInitialPositions() {
        Map<String, Object> result = new HashMap<>(16);

        carService.randomCar(carNums);
        passengerService.randomPassenger(passengerNums);

        // 获取五辆小车的初始位置
        List<DidiCar> cars = carService.getCars();
        result.put("cars", cars);

        // 获取三个乘客的初始位置和目的地
        List<DidiPassenger> passengers = passengerService.getPassengers();
        result.put("passengers", passengers);
        logger.info("所有状态已重置，小车和乘客的初始位置已生成");
        return result;
    }
}