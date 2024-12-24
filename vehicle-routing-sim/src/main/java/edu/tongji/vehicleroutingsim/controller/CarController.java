package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * <p>
 * 设置小车位姿
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:29
 */
@RestController
public class CarController {


    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * 设置车辆信息
     * @param carIndex 车辆索引
     * @param row 行
     * @param col 列
     * @param angle 角度
     */
    @GetMapping("/setCarInfo")
    public String setCarInfo(
        @RequestParam("carIndex") int carIndex,
        @RequestParam("row") double row,
        @RequestParam("col") double col,
        @RequestParam("angle") double angle) {
        carService.setCar(carIndex, row, col, angle);
        // 返回数据的逻辑
        return "小车位置已设置：索引:" + carIndex + ",位置:" + row + "," + col + ",角度:" + angle;
    }
}