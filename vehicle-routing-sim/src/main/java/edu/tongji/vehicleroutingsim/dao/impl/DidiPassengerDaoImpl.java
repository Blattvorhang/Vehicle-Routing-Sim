package edu.tongji.vehicleroutingsim.dao.impl;

import edu.tongji.vehicleroutingsim.dao.DidiPassengerDao;
import edu.tongji.vehicleroutingsim.model.DidiPassenger;
import edu.tongji.vehicleroutingsim.model.PassengerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p>
 * 乘客数据访问接口实现类
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 11:37
 */
@Repository
public class DidiPassengerDaoImpl implements DidiPassengerDao {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(DidiPassengerDaoImpl.class);

    /**
     * 乘客列表
     */
    private final List<DidiPassenger> passengers = new ArrayList<>();


    /**
     * 获取乘客列表
     *
     * @return 乘客列表
     */
    @Override
    public List<DidiPassenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    /**
     * 添加乘客
     *
     * @param passengerIndex 乘客索引
     * @param startX         乘客起点X坐标
     * @param startY         乘客起点Y坐标
     * @param endX           乘客终点X坐标
     * @param endY           乘客终点Y坐标
     */
    @Override
    public void addPassenger(int passengerIndex, double startX, double startY, double endX, double endY) {
        if(passengerIndex < 0) {
            logger.error("乘客索引不能为负数");
            throw new IllegalArgumentException("乘客索引不能为负数");
        }
        passengers.add(new DidiPassenger(passengerIndex, startX, startY, endX, endY));
        logger.info("添加乘客成功, 乘客索引:{},乘客初始位置:{},{},乘客目的地:{},{}",passengerIndex, startX, startY, endX, endY);
    }

    /**
     * 根据乘客索引获取乘客
     *
     * @param passengerIndex 乘客索引
     * @return 对应的乘客对象
     */
    @Override
    public DidiPassenger getPassengerByIndex(int passengerIndex) {
        if(passengerIndex < 0 || passengerIndex >= passengers.size()) {
            logger.error("不存在的乘客索引");
            throw new IllegalArgumentException("不存在的乘客索引");
        }
        return passengers.stream()
                .filter(passenger -> passenger.getPassengerIndex() == passengerIndex)
                .findFirst()
                .orElse(null);
    }

    /**
     * 接到乘客
     *
     * @param passengerIndex 乘客索引
     */
    @Override
    public void pickUpPassenger(int carIndex,int passengerIndex) {
        DidiPassenger passenger =getPassengerByIndex(passengerIndex);
        passenger.setStatus(PassengerStatus.ONBOARD);
        passenger.setNowCarIndex(carIndex);
    }

    /**
     * 乘客下车
     *
     * @param passengerIndex 乘客索引
     */
    @Override
    public void dropOffPassenger(int passengerIndex) {
        getPassengerByIndex(passengerIndex).setStatus(PassengerStatus.ARRIVED);
    }

    /**
     * 清空所有乘客
     */
    @Override
    public void clearPassengers() {
        passengers.clear();
    }
}
