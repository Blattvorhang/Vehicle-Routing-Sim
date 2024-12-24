package edu.tongji.vehicleroutingsim.service;

import edu.tongji.vehicleroutingsim.dao.DidiMapDao;
import edu.tongji.vehicleroutingsim.dao.DidiPassengerDao;
import edu.tongji.vehicleroutingsim.dao.impl.DidiCarDaoImpl;
import edu.tongji.vehicleroutingsim.dao.impl.DidiMapDaoImpl;
import edu.tongji.vehicleroutingsim.dao.impl.DidiPassengerDaoImpl;
import edu.tongji.vehicleroutingsim.model.DidiPassenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * <p>
 * 乘客服务类。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 12:23
 */
@Service
public class PassengerService {

    private static final Logger logger = LoggerFactory.getLogger(PassengerService.class);
    private final DidiMapDao mapDaoImpl;
    private final DidiPassengerDao passengerDaoImpl;

    @Autowired
    public PassengerService(DidiMapDaoImpl mapDaoImpl, DidiPassengerDaoImpl passengerDaoImpl) {
        this.mapDaoImpl = mapDaoImpl;
        this.passengerDaoImpl = passengerDaoImpl;
    }

    public void randomPassenger(int passengerNum) {
        passengerDaoImpl.clearPassengers();
        int maxX = mapDaoImpl.getMapCols();
        int maxY = mapDaoImpl.getMapRows();
        for (int i = 0; i < passengerNum; i++) {
            int startX = (int) (Math.random() * maxX);
            int startY = (int) (Math.random() * maxY);
            if (mapDaoImpl.isObstacle(startX, startY)) {
                i--;
                continue;
            }
            int endX = (int) (Math.random() * maxX);
            int endY = (int) (Math.random() * maxY);
            if (mapDaoImpl.isObstacle(endX, endY)) {
                i--;
                continue;
            }
            //起点终点距离太近
            if (Math.abs(startX - endX) + Math.abs(startY - endY) < mapDaoImpl.getMapCols() / 4) {
                i--;
                continue;
            }
            passengerDaoImpl.addPassenger(i, startX, startY, endX, endY);
        }
    }

    public void pickUpPassenger(int carIndex, int passengerIndex) {
        passengerDaoImpl.pickUpPassenger(carIndex, passengerIndex);
        logger.info("乘客{}上了{}号车", passengerIndex, carIndex);
    }

    public void dropOffPassenger(int carIndex) {
        passengerDaoImpl.dropOffPassenger(carIndex);
        logger.info("{}号车乘客下车", carIndex);
    }

    public void showPassengers() {
        List<DidiPassenger> passengers = passengerDaoImpl.getPassengers();
        for (DidiPassenger passenger : passengers) {
            logger.info("乘客索引:{},乘客初始位置:{},{},乘客目的地:{},{}", passenger.getPassengerIndex(), passenger.getStartX(), passenger.getStartY(), passenger.getEndX(), passenger.getEndY());
        }
    }

    public List<DidiPassenger> getPassengers() {
        return passengerDaoImpl.getPassengers();
    }
}
