package edu.tongji.vehicleroutingsim.service;

import edu.tongji.vehicleroutingsim.dao.DidiMapDao;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 * 地图服务类。
 * <p>
 * 该类提供了地图的管理功能，包括从文件加载地图、保存地图、设置和获取地图数据等。
 * 支持对黑白位图地图文件和序列化地图对象文件的处理。
 * </p>
 *
 * 配置路径说明：
 * <ul>
 *     <li><b>map.object.filepath</b>：序列化地图对象文件的目录路径。</li>
 *     <li><b>map.object.filename</b>：序列化地图对象文件的文件名。</li>
 *     <li><b>map.file</b>：黑白位图地图文件的路径（支持类路径资源）。</li>
 * </ul>
 *
 * 示例配置（application.properties 或 application.yml）：
 * <pre>
 * # 序列化地图对象文件路径和文件名
 * map.object.filepath=map_object
 * map.object.filename=map_object.ser
 *
 * # 黑白位图地图文件路径
 * map.file=classpath:map/map_image.bmp
 * </pre>
 *
 * 主要功能：
 * <ul>
 *     <li>从黑白位图文件加载地图信息</li>
 *     <li>从序列化的地图对象文件加载地图</li>
 *     <li>将地图数据保存到序列化文件中</li>
 *     <li>提供对地图数据的读写访问</li>
 * </ul>
 *
 * 注意事项：
 * <ul>
 *     <li>使用前需要确保配置文件中已正确设置地图文件路径和名称</li>
 *     <li>支持对文件夹路径的自动创建，但需要确保程序有写入权限</li>
 * </ul>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 5:40
 */
@Service
public class MapService {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(MapService.class);

    /**
     * 地图对象文件路径，从配置文件中加载
     */
    private final Resource mapObjectResource;

    /**
     * 地图位图文件路径，从配置文件中加载（支持类路径资源）
     */
    private final Resource mapFileResource;

    /**
     * 地图 DAO，用于与数据存储层交互
     */
    private final DidiMapDao didiMapDao;

    /**
     * 使用构造函数注入
     */
    @Autowired
    public MapService(
            @Value("${map.object.filepath}") Resource mapObjectResource,
            @Value("${map.file}") Resource mapFileResource,
            DidiMapDao didiMapDao) {
        this.mapObjectResource = mapObjectResource;
        this.mapFileResource = mapFileResource;
        this.didiMapDao = didiMapDao;
    }

    /**
     * 初始化方法，检查地图位图文件路径是否存在
     *
     * @throws IllegalStateException 如果文件路径无效，则抛出异常
     */
    @PostConstruct
    public void validateFilePaths() {
        // 检查地图位图文件路径
        try {
            if (!mapFileResource.exists()) {
                logger.warn("地图位图文件路径不存在：{}", mapFileResource.getFile().getPath());
            }
        } catch (IOException e) {
            logger.error("地图位图文件路径不存在：{}", e.getMessage());
        }
    }

    /**
     * 保存地图对象到指定文件
     * 1. 检查父目录是否存在，不存在则创建
     * 2. 调用 DAO 保存地图对象到文件
     *
     * @throws IllegalStateException 如果无法创建父目录，则抛出异常
     */
    public void saveMapObject() {
        File file;
        try {
            file = mapObjectResource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 检查并创建父目录
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                logger.error("无法创建目录：{}", parentDir.getPath());
                throw new IllegalStateException("无法创建目录：" + parentDir.getPath());
            }
        }

        logger.info("正在保存地图对象到文件：{}", file.getPath());
        didiMapDao.saveMapObject(file);

    }

    /**
     * 从指定文件中加载地图对象
     * 1. 检查文件是否存在
     * 2. 调用 DAO 加载地图对象
     *
     * @throws IllegalStateException 如果文件不存在，则抛出异常
     */
    public boolean loadMapObject() {
        File mapObjectFile;
        try {
            mapObjectFile = mapObjectResource.getFile();
            logger.info("正在加载地图对象文件：{}", mapObjectFile.getAbsoluteFile());
            didiMapDao.loadMapObject(mapObjectFile);
        } catch (IOException |ClassNotFoundException e) {
            logger.error("无法加载地图对象文件：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 从黑白位图文件中加载地图信息
     * 1. 检查位图文件是否存在
     * 2. 调用 DAO 加载地图文件
     *
     * @throws IllegalStateException 如果文件不存在或加载失败，则抛出异常
     */
    public void loadMapFile() {
        try {
            File mapFile = mapFileResource.getFile();

            if (!mapFile.exists() || !mapFile.isFile()) {
                logger.error("地图文件不存在：{}", mapFile.getPath());
                throw new IllegalStateException("地图文件不存在：" + mapFile.getPath());
            }

            logger.info("正在加载地图文件：{}", mapFile.getPath());
            didiMapDao.loadMapFile(mapFile);
        } catch (IOException e) {
            logger.error("无法加载地图文件：{}", e.getMessage());
            throw new IllegalStateException("无法加载地图文件：" + e.getMessage(), e);
        }
    }

    /**
     * 获取地图对象
     *
     * @return 地图对象
     */
    public boolean[][] getMap() {
        boolean[][] map = didiMapDao.getMap();
        if (null == map) {
            throw new IllegalStateException("地图对象为空");
        } else {
            return map;
        }
    }

    /**
     * 设置地图对象
     *
     * @param map 地图对象
     */
    public void setMap(boolean[][] map) {
        didiMapDao.updateMap(map);
    }

    /**
     * 打印地图到控制台
     */
    public void reverseMap() {
        didiMapDao.reverseMap();
    }

    /**
     * 获取反转地图
     * @return 反转地图
     */
    public boolean[][] getReverseMap() {
        return didiMapDao.getReverseMap();
    }

    public boolean isObstacle(double row, double col) {
        return didiMapDao.isObstacle((int)row, (int)col);
    }
}
