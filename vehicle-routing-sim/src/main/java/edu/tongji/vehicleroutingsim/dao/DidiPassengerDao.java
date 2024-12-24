package edu.tongji.vehicleroutingsim.dao;

import edu.tongji.vehicleroutingsim.model.DidiPassenger;

import java.util.List;

/**
 * Description:
 * <p>
 * 描述类的功能与作用（这里补充具体内容）。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 11:32
 */
public interface DidiPassengerDao {
    /**
     * 获取乘客列表
     * @return 乘客列表
     */
    List<DidiPassenger> getPassengers();

    /**
     * 添加乘客
     * @param passengerIndex 乘客索引
     * @param startX 乘客起点X坐标
     * @param startY 乘客起点Y坐标
     * @param endX 乘客终点X坐标
     * @param endY 乘客终点Y坐标
     */
    void addPassenger(int passengerIndex, double startX, double startY, double endX, double endY);
    /**
     * 根据乘客索引获取乘客
     * @param passengerIndex 乘客索引
     * @return 对应的乘客对象
     */
    DidiPassenger getPassengerByIndex(int passengerIndex);
    /**
     * 接到乘客
     * @param passengerIndex 乘客索引
     * @param carIndex 车辆索引
     */
    void pickUpPassenger(int carIndex,int passengerIndex);
    /**
     * 乘客下车
     * @param passengerIndex 乘客索引
     */
    void dropOffPassenger(int passengerIndex);

    /**
     * 清空所有乘客
     */
    void clearPassengers();
}
