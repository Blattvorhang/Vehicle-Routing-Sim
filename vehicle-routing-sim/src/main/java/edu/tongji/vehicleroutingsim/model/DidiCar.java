package edu.tongji.vehicleroutingsim.model;

import lombok.Data;

/**
 * Description:
 * <p>
 * 车辆数据类
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:39
 */
@Data
public class DidiCar {
    private int carIndex;

    private int nowPassengerIndex;

    boolean hasPassenger;
    /**
     * 车辆位置，行是Y，列是X
     */
    private double carX;
    private double carY;

    private double carAngle;

    public DidiCar(int carIndex, double carX, double carY, double carAngle){
        this.carIndex = carIndex;
        this.carX = carX;
        this.carY = carY;
        this.carAngle = carAngle;
        this.nowPassengerIndex = -1;
    }
}
