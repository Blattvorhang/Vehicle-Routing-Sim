package edu.tongji.vehicleroutingsim.service;

import edu.tongji.vehicleroutingsim.dao.impl.DidiCarDaoImpl;
import edu.tongji.vehicleroutingsim.dao.impl.DidiMapDaoImpl;
import edu.tongji.vehicleroutingsim.model.DidiCar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * <p>
 * 小车服务类
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:59
 */
@Service
public class CarService {

    private final DidiCarDaoImpl didiCarDaoImpl;

    private final DidiMapDaoImpl mapDaoImpl;

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    public CarService(DidiCarDaoImpl didiCarDaoImpl, DidiMapDaoImpl mapDaoImpl) {
        this.didiCarDaoImpl = didiCarDaoImpl;
        this.mapDaoImpl = mapDaoImpl;
    }

    /**
     * 设置小车位置
     *
     * @param carIndex 小车索引
     * @param carX     小车X坐标
     * @param carY     小车Y坐标
     * @param carAngle 小车角度
     */
    public void setCar(int carIndex, double carX, double carY, double carAngle){
        didiCarDaoImpl.updateCar(carIndex, carX, carY, carAngle);
        logger.info("小车位置已设置：索引:{},位置:{},{},角度:{}", carIndex, carX, carY, carAngle);
    }


    public int getCarPassengerIndex(int carIndex){
        return didiCarDaoImpl.selectById(carIndex).getNowPassengerIndex();
    }

    /**
     * 随机生成小车
     */
    public void randomCar(int carNum) {
        didiCarDaoImpl.deleteCars();
        int maxX = mapDaoImpl.selectMapCols() - 1;
        int maxY = mapDaoImpl.selectMapRows() - 1;
        for (int i = 0; i < carNum; i++) {
            // 随机生成小车位置(0,maxX) (0,maxY)
            double carX = Math.random() * maxX;
            double carY = Math.random() * maxY;
            if (mapDaoImpl.isObstacle((int) carX, (int) carY)) {
                i--;
                continue;
            }
            didiCarDaoImpl.insertCar(i, carX, carY, 0);
        }
    }

    public List<DidiCar> getCars(){
        return didiCarDaoImpl.selectCars();
    }

    public void pickPassenger(int carIndex, int passengerIndex) {
        didiCarDaoImpl.pickPassenger(carIndex, passengerIndex);
        logger.info("小车{}接到乘客{}", carIndex, passengerIndex);
    }

    public void dropPassenger(int carIndex) {
        didiCarDaoImpl.dropPassenger(carIndex);
        logger.info("小车{}送乘客到达目的地", carIndex);
    }
}
