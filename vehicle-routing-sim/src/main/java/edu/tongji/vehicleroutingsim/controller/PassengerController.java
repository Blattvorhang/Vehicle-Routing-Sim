package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.model.DidiPassenger;
import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:
 * <p>
 * 乘客控制器。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/27 16:50
 */
@Controller
public class PassengerController {
    private final CarService carService;

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(CarService carService, PassengerService passengerService) {
        this.carService = carService;
        this.passengerService = passengerService;
    }

    /**
     * 小车接乘客的接口
     *
     * @param carIndex       小车索引
     * @return 接送操作结果
     */
    @GetMapping("/api/passenger/drop")
    @ResponseBody
    public String dropPassenger(
            @RequestParam("carIndex") int carIndex) {
        // 执行接送逻辑
        int passengerIndex = carService.getCarPassengerIndex(carIndex);
        carService.dropPassenger(carIndex);
        passengerService.dropOffPassenger(carIndex);
        return "小车" + carIndex + "送乘客" + passengerIndex + "到站";
    }

    /**
     * 小车接乘客的接口
     *
     * @param carIndex       小车索引
     * @param passengerIndex 乘客索引
     * @return 接送操作结果
     */
    @GetMapping("/api/passenger/pick")
    @ResponseBody
    public String pickPassenger(
            @RequestParam("carIndex") int carIndex,
            @RequestParam("passengerIndex") int passengerIndex) {
        // 执行接送逻辑
        carService.pickPassenger(carIndex, passengerIndex);
        passengerService.pickUpPassenger(carIndex, passengerIndex);
        return "小车" + carIndex + "接乘客" + passengerIndex + "成功";
    }

    /**
     * 获取乘客信息
     *
     * @param passengerIndex 乘客索引
     * @return 乘客信息
     */
    @GetMapping("/api/passenger/select")
    @ResponseBody
    public DidiPassenger selectPassenger(@RequestParam("passengerIndex") int passengerIndex) {
        return passengerService.getPassenger(passengerIndex);
    }

    /**
     * 获取所有乘客信息
     *
     * @return 乘客信息
     */
    @GetMapping("/api/passenger/select/all")
    @ResponseBody
    public List<DidiPassenger> selectPassengers() {
        return passengerService.getPassengers();
    }
}
