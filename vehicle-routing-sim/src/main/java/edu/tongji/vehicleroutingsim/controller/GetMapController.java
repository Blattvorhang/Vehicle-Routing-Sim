package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.service.MapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * <p>
 * 获取地图map信息
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 16:18
 */
@RestController
public class GetMapController {
    private static final Logger logger = LoggerFactory.getLogger(InitPositionController.class);
    private final MapService mapService;

    @Autowired
    public GetMapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 获取地图信息
     *
     * @return 包含地图信息的 Map
     */
    @GetMapping("/api/map/getMap")
    public boolean[][] getMap() {
        logger.info("已提供地图信息");
        return mapService.getMap();
    }
}
