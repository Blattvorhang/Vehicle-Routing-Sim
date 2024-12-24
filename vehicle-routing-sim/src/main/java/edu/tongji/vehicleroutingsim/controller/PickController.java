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
 * 接到乘客的接口
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 13:53
 */

@RestController
public class PickController {

    private final CarService carService;
    private final PassengerService passengerService;

    @Autowired
    public PickController(CarService carService, PassengerService passengerService) {
        this.carService = carService;
        this.passengerService = passengerService;
    }

    /**
     * 小车接乘客的接口
     *
     * @param carIndex       小车索引
     * @param passengerIndex 乘客索引
     * @return 接送操作结果
     */
    @GetMapping("/pickPassenger")
    public String pickPassenger(
            @RequestParam("carIndex") int carIndex,
            @RequestParam("passengerIndex") int passengerIndex) {
        // 执行接送逻辑
        carService.pickPassenger(carIndex, passengerIndex);
        passengerService.pickUpPassenger(carIndex, passengerIndex);
        return "小车" + carIndex + "接乘客" + passengerIndex + "成功";
    }
}