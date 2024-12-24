package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.model.DidiCar;
import edu.tongji.vehicleroutingsim.model.DidiPassenger;
import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p>
 * 展示所有小车和乘客的信息
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 14:03
 */
@RestController
public class ShowController {

    private final CarService carService;

    private final PassengerService passengerService;

    @Autowired
    public ShowController(CarService carService, PassengerService passengerService) {
        this.carService = carService;
        this.passengerService = passengerService;
    }

    @GetMapping("/show")
    public Map<String, Object> getInitialPositions() {
        Map<String, Object> result = new HashMap<>(16);

        // 获取五辆小车的初始位置
        List<DidiCar> cars = carService.getCars();
        result.put("cars", cars);

        // 获取三个乘客的初始位置和目的地
        List<DidiPassenger> passengers = passengerService.getPassengers();
        result.put("passengers", passengers);

        return result;
    }
}
