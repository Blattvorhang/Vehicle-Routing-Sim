package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * <p>
 * 乘客到站
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 14:11
 */
@RestController
public class DropController {
    private final CarService carService;
    private final PassengerService passengerService;

    @Autowired
    public DropController(CarService carService, PassengerService passengerService) {
        this.carService = carService;
        this.passengerService = passengerService;
    }

    @GetMapping("/api/car/dropPassenger")
    public String pickPassenger(
            @RequestParam("carIndex") int carIndex) {
        // 执行接送逻辑
        int passengerIndex = carService.getCarPassengerIndex(carIndex);
        carService.dropPassenger(carIndex);
        passengerService.dropOffPassenger(carIndex);
        return "小车" + carIndex + "送乘客" + passengerIndex + "到站";
    }
}
