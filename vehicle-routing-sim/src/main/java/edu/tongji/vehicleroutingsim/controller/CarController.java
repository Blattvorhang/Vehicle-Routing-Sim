package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.model.DidiCar;
import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.MapService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * <p>
 * 小车控制器
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:29
 */
@RestController
public class CarController {


    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;
    private final MapService mapService;

    @Autowired
    public CarController(CarService carService, MapService mapService) {
        this.carService = carService;
        this.mapService = mapService;
    }

    /**
     * 设置车辆信息
     *
     * @param carIndex 车辆索引
     * @param row      行
     * @param col      列
     * @param angle    角度
     */
    @GetMapping("/api/car/update")
    public String updateCarInfo(
            @RequestParam("carIndex") int carIndex,
            @RequestParam("row") double row,
            @RequestParam("col") double col,
            @RequestParam("angle") double angle, HttpServletRequest request) {
        carService.setCar(carIndex, row, col, angle);
        boolean error = mapService.isObstacle(row, col);
        logger.info("ip地址为：" + request.getRemoteAddr() + "的用户已设置小车" + carIndex + "的位置");
        // 返回数据的逻辑
        return "小车位置已设置：索引:" + carIndex + ",位置:" + row + "," + col + ",角度:" + angle + ",是否有障碍物:" + error;
    }

    /**
     * 获取车辆信息
     *
     * @param carIndex 车辆索引
     * @return 车辆信息
     */
    @GetMapping("/api/car/select")
    public DidiCar selectCar(@RequestParam("carIndex") int carIndex) {
        logger.info("已提供小车" + carIndex + "的信息");
        return carService.getCars().get(carIndex);
    }

    /**
     * 获取所有车辆信息
     *
     * @return 车辆信息
     */
    @GetMapping("/api/car/select/all")
    public List<DidiCar> selectCars() {
        logger.info("已提供所有小车的信息");
        return carService.getCars();
    }
}