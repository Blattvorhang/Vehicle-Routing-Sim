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
 * 地图控制器
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/24 16:18
 */
@RestController
public class MapController {
    private static final Logger logger = LoggerFactory.getLogger(InitController.class);
    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 获取地图信息
     *
     * @return 包含地图信息的 Map
     */
    @GetMapping("/api/map/select")
    public boolean[][] selectMap() {
        logger.info("已提供地图信息");
        boolean[][] map = mapService.getMap();
        if(map == null) {
            logger.error("地图信息为空,正在尝试重新读取地图信息");
            mapService.loadMapFile();
            mapService.reverseMap();
            mapService.saveMapObject();
            mapService.loadMapObject();
        }
        return mapService.getReverseMap();
    }
}
