package edu.tongji.vehicleroutingsim.dao;

import edu.tongji.vehicleroutingsim.model.DidiCar;

import java.util.List;

/**
 * Description:
 * <p>
 * 小车数据访问接口
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:44
 */
public interface DidiCarDao {

    /**
     * 加载车辆数据
     * @return 车辆数据
     */
    List<DidiCar> selectCars();

    /**
     * 添加车辆
     * @param carIndex 车辆索引
     * @param carX 车辆X坐标
     * @param carY 车辆Y坐标
     * @param carAngle 车辆角度
     */
    void insertCar(int carIndex, double carX, double carY, double carAngle);

    /**
     * 根据车辆索引获取车辆
     * @param carIndex 车辆索引
     * @return 对应的车辆对象
     */
    DidiCar selectById(int carIndex);

    /**
     * 设置车辆位置
     * @param carIndex 车辆索引
     * @param carX 车辆X坐标
     * @param carY 车辆Y坐标
     * @param carAngle 车辆角度
     */
    void updateCar(int carIndex, double carX, double carY, double carAngle);

    /**
     * 清空所有车辆
     */
    void deleteCars();

    /**
     * 接乘客
     * @param carIndex 车辆索引
     * @param passengerIndex 乘客索引
     */
    void pickPassenger(int carIndex, int passengerIndex);

    /**
     * 送乘客
     * @param carIndex 车辆索引
     */
    void dropPassenger(int carIndex);

}
