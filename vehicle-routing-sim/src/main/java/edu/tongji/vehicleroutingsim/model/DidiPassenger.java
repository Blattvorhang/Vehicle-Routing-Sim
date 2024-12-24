package edu.tongji.vehicleroutingsim.model;

import lombok.Data;

/**
 * Description:
 * <p>
 * 描述类的功能与作用（这里补充具体内容）。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 11:25
 */
@Data
public class DidiPassenger{
    private int passengerIndex;
    private PassengerStatus status;
    private int nowCarIndex;
    private double startX;
    private double startY;

    private double endX;
    private double endY;

    public DidiPassenger(int passengerIndex, double startX, double startY, double endX, double endY){
        this.passengerIndex = passengerIndex;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.status = PassengerStatus.WAITING;
        this.nowCarIndex = -1;
    }

}
