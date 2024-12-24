package edu.tongji.vehicleroutingsim.service;

import edu.tongji.vehicleroutingsim.dao.impl.DidiCarDaoImpl;
import edu.tongji.vehicleroutingsim.dao.impl.DidiMapDaoImpl;
import edu.tongji.vehicleroutingsim.dao.impl.DidiPassengerDaoImpl;
import edu.tongji.vehicleroutingsim.model.DidiCar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * <p>
 * 描述类的功能与作用（这里补充具体内容）。
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
    public CarService(DidiCarDaoImpl didiCarDaoImpl, DidiMapDaoImpl mapDaoImpl, DidiPassengerDaoImpl passengerDaoImpl) {
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
        didiCarDaoImpl.setCar(carIndex, carX, carY, carAngle);
        logger.info("小车位置已设置：索引:{},位置:{},{},角度:{}", carIndex, carX, carY, carAngle);
    }

    /**
     * 获取小车角度
     *
     * @param carIndex 小车索引
     * @return 小车角度
     */
    double getCarAngle(int carIndex){
        return didiCarDaoImpl.getCarByIndex(carIndex).getCarAngle();
    }

    /**
     * 获取小车位姿
     *
     * @param carIndex 小车索引
     * @return 小车位置，索引0为X坐标，索引1为Y坐标，索引2为角度
     */
    double[] getCarPosition(int carIndex){
        double[] car = new double[3];
        car[0] = didiCarDaoImpl.getCarByIndex(carIndex).getCarX();
        car[1] = didiCarDaoImpl.getCarByIndex(carIndex).getCarY();
        car[2] = didiCarDaoImpl.getCarByIndex(carIndex).getCarAngle();
        return car;
    }

    public int getCarPassengerIndex(int carIndex){
        return didiCarDaoImpl.getCarByIndex(carIndex).getNowPassengerIndex();
    }

    /**
     * 随机生成小车
     */
    public void randomCar(int carNum) {
        didiCarDaoImpl.clearCars();
        int maxX = mapDaoImpl.getMapCols();
        int maxY = mapDaoImpl.getMapRows();
        for (int i = 0; i < carNum; i++) {
            // 随机生成小车位置(0,maxX) (0,maxY)
            double carX = Math.random() * maxX;
            double carY = Math.random() * maxY;
            if (mapDaoImpl.isObstacle((int) carX, (int) carY)) {
                i--;
                continue;
            }
            didiCarDaoImpl.addCar(i, carX, carY, 0);
        }
    }

    public void showCar(){
        for(int i = 0; i < didiCarDaoImpl.getCarNum(); i++){
            DidiCar car = didiCarDaoImpl.getCarByIndex(i);
            logger.info("小车索引:{},位置:{},{},角度:{},是否有乘客{}", car.getCarIndex(), car.getCarX(), car.getCarY(), car.getCarAngle(), car.isHasPassenger());
            if(car.isHasPassenger()){
                logger.info("车内乘客索引:{}", car.getNowPassengerIndex());
            }
        }
    }

    public List<DidiCar> getCars(){
        return didiCarDaoImpl.getCars();
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
