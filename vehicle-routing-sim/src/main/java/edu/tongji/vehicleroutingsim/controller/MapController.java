package edu.tongji.vehicleroutingsim.controller;

import edu.tongji.vehicleroutingsim.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * <p>
 * 描述类的功能与作用（这里补充具体内容）。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 10:29
 */
@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 获取地图数组。
     *
     * @return 布尔二维数组表示的地图数据
     */
    @GetMapping
    public boolean[][] getMap() {
        return mapService.getMap();
    }
}