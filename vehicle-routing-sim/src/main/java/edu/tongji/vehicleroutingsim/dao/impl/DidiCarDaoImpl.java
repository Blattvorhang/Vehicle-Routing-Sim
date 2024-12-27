package edu.tongji.vehicleroutingsim.dao.impl;

import edu.tongji.vehicleroutingsim.dao.DidiCarDao;
import edu.tongji.vehicleroutingsim.model.DidiCar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p>
 * 小车数据访问接口实现类
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:56
 */
@Repository
public class DidiCarDaoImpl implements DidiCarDao {

    /**
     * 车辆列表
     */
    private final List<DidiCar> cars = new ArrayList<>();

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(DidiCarDaoImpl.class);

    /**
     * 添加车辆数据
     */
    @Override
    public void insertCar(int carIndex, double carX, double carY, double carAngle) {
        // 检查车辆索引是否重复
        if (cars.stream().anyMatch(car -> car.getCarIndex() == carIndex)) {
            logger.error("车辆索引重复");
            throw new IllegalArgumentException("车辆索引重复");
        }

        // 创建车辆对象
        DidiCar car = new DidiCar(carIndex, carX, carY, carAngle);
        car.setCarIndex(carIndex);
        car.setCarX(carX);
        car.setCarY(carY);
        car.setCarAngle(carAngle);
        cars.add(car);
        logger.info("车辆添加成功，小车索引:{},初始位置:{},{}", carIndex, carX, carY);
    }

    /**
     * 根据车辆索引获取车辆
     *
     * @param carIndex 车辆索引
     * @return 对应的车辆对象
     */
    @Override
    public DidiCar selectById(int carIndex) {
        if(carIndex < 0 || carIndex >= cars.size()) {
            logger.error("车辆索引错误");
            throw new IllegalArgumentException("车辆索引错误");
        }

        return cars.stream()
                .filter(car -> car.getCarIndex() == carIndex)
                .findFirst()
                .orElse(null);
    }

    /**
     * 设置车辆位置
     *
     * @param carIndex 车辆索引
     * @param carX     车辆X坐标
     * @param carY     车辆Y坐标
     * @param carAngle 车辆角度
     */
    @Override
    public void updateCar(int carIndex, double carX, double carY, double carAngle) {
        // 获取车辆对象
        DidiCar car = selectById(carIndex);
        if (car != null) {
            // 更新车辆位置
            car.setCarX(carX);
            car.setCarY(carY);
            car.setCarAngle(carAngle);
        }
    }

    /**
     * 清空所有车辆
     */
    @Override
    public void deleteCars() {
        cars.clear();
    }

    /**
     * 获取所有车辆数据
     * @return 车辆数据
     */
    @Override
    public List<DidiCar> selectCars() {
        return new ArrayList<>(cars);
    }

    /**
     * 设置乘客
     *
     * @param carIndex    车辆索引
     * @param passengerIndex 乘客索引
     */
    @Override
    public void pickPassenger(int carIndex,int passengerIndex) {
        // 获取车辆对象
        DidiCar car = selectById(carIndex);
        if(car.isHasPassenger()){
            logger.error("车辆已有乘客");
            throw new IllegalArgumentException("车辆已有乘客");
        }
        car.setNowPassengerIndex(passengerIndex);
        car.setHasPassenger(true);
    }

    /**
     * 乘客下车
     *
     * @param carIndex 车辆索引
     */
    @Override
    public void dropPassenger(int carIndex) {
        // 获取车辆对象
        DidiCar car = selectById(carIndex);
        if(!car.isHasPassenger()){
            logger.error("车辆没有乘客");
            throw new IllegalArgumentException("车辆没有乘客");
        }
        car.setNowPassengerIndex(-1);
        car.setHasPassenger(false);
    }
}
