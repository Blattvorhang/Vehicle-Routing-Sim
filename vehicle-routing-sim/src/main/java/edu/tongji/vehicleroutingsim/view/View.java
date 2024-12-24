package edu.tongji.vehicleroutingsim.view;

import edu.tongji.vehicleroutingsim.service.CarService;
import edu.tongji.vehicleroutingsim.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <p>
 * 描述类的功能与作用（这里补充具体内容）。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 15:14
 */
@Component
public class View {
    private final CarService carService;
    private final PassengerService passengerService;

    @Autowired
    public View(CarService carService, PassengerService passengerService) {
        this.carService = carService;
        this.passengerService = passengerService;
    }
}
