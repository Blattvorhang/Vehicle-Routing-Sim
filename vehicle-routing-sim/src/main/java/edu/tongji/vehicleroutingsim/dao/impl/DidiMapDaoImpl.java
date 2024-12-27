package edu.tongji.vehicleroutingsim.dao.impl;

import edu.tongji.vehicleroutingsim.dao.DidiMapDao;
import edu.tongji.vehicleroutingsim.model.DidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Description:
 * <p>
 * DidiMapDao 的实现类，提供对地图数据的存取功能。
 * 支持从文件夹保存/加载地图对象，以及从黑白位图文件加载地图信息。
 * </p>
 *
 * 功能包括：
 * <ul>
 *     <li>保存和加载序列化的地图对象文件</li>
 *     <li>从黑白位图文件解析地图信息</li>
 *     <li>提供对地图二维数组的访问和更新操作</li>
 * </ul>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 5:30
 */
@Repository
public class DidiMapDaoImpl implements DidiMapDao {


    private DidiMap didiMap;

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(DidiMapDaoImpl.class);

    /**
     * 构造函数，使用依赖注入初始化地图对象。
     *
     * @param didiMap DidiMap 实例，表示地图对象
     */
    @Autowired
    public DidiMapDaoImpl(DidiMap didiMap) {
        this.didiMap = didiMap;
    }

    /**
     * 获取当前地图的布尔二维数组表示。
     *
     * @return 当前地图的布尔二维数组，true 表示障碍物，false 表示空地
     */
    @Override
    public boolean[][] getMap() {
        return didiMap.getMap();
    }

    /**
     * 更新当前地图的布尔二维数组表示。
     *
     * @param map 新的布尔二维数组，true 表示障碍物，false 表示空地
     */
    @Override
    public void updateMap(boolean[][] map) {
        didiMap.setMap(map);
    }

    /**
     * 保存地图对象到文件。
     *
     * @param mapObjectFile 地图对象序列化文件
     */
    @Override
    public void saveMapObject(File mapObjectFile) {
        // 检查文件夹是否存在，不存在则创建
        if (!mapObjectFile.exists()) {
            boolean created = mapObjectFile.mkdirs();
            if (!created) {
                logger.error("无法创建目录：{}", mapObjectFile.getPath());
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(mapObjectFile))) {
            // 将地图对象写入文件
            oos.writeObject(didiMap);
            logger.info("地图信息已保存到文件：{}", mapObjectFile.getAbsoluteFile());
        } catch (IOException e) {
            logger.error("保存地图信息失败：{}", e.getMessage());
        }
    }


    /**
     * 从地图对象序列化文件中读取地图。
     *
     * @param mapObjectFile 地图对象序列化文件
     */
    @Override
    public void loadMapObject(File mapObjectFile) throws IOException, ClassNotFoundException {
        logger.info("正在加载地图对象文件：{}", mapObjectFile.getAbsoluteFile());
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mapObjectFile))) {
            didiMap = (DidiMap) ois.readObject();
            logger.info("地图信息已从文件加载：{}，大小{},{}", mapObjectFile.getPath(), didiMap.getMap().length, didiMap.getMap()[0].length);
        }
    }

    /**
     * 从黑白位图文件中加载地图信息。
     *
     * @param mapFile 地图文件
     */
    @Override
    public void loadMapFile(File mapFile) {
        try {
            // 使用 ImageIO 读取黑白位图文件
            BufferedImage image = ImageIO.read(mapFile);

            if (image == null) {
                throw new IllegalArgumentException("无法读取图片文件：" + mapFile.getPath());
            }

            // 获取图片的宽度和高度
            int rows = image.getHeight();
            int cols = image.getWidth();

            // 初始化地图的二维数组
            boolean[][] map = new boolean[rows][cols];

            // 遍历每个像素点，判断是否为黑色（障碍物）
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // 获取像素的 RGB 值
                    int rgb = image.getRGB(col, row);
                    // 白色表示障碍物
                    // 其他颜色表示空地
                    map[row][col] = !isBlack(rgb);
                }
            }

            didiMap.setMap(map);
            logger.info("地图信息已从图片加载：{}", mapFile.getPath());

        } catch (IOException e) {
            logger.error("加载地图文件失败：{}", e.getMessage());
        }
    }

    /**
     * 判断一个 RGB 值是否为黑色。
     *
     * @param rgb RGB 值
     * @return 如果 RGB 值接近 0（黑色），则返回 true；否则返回 false
     */
    private boolean isBlack(int rgb) {
        // 提取 RGB 的 R、G、B 三个分量
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // 如果 RGB 的值接近 0（黑色），则认为是道路
        // 黑色的容差范围
        return red < 50 && green < 50 && blue < 50;
    }

    /**
     * 获取地图行数
     *
     * @return 地图行数
     */
    @Override
    public int selectMapRows() {
        return didiMap.getMap().length;
    }

    /**
     * 获取地图列数
     *
     * @return 地图列数
     */
    @Override
    public int selectMapCols() {
        return didiMap.getMap()[0].length;
    }

    /**
     * 获取地图中指定位置是否为障碍物
     *
     * @param row 行索引
     * @param col 列索引
     * @return 是否为障碍物
     */
    @Override
    public boolean isObstacle(int row, int col) {
        if(row >= didiMap.getMap().length || col >= didiMap.getMap()[0].length) {
            return true;
        }
        return didiMap.getMap()[row][col];
    }

    /**
     * 获取地图的反向表示
     *
     * @return 反向表示的地图
     */
    @Override
    public boolean[][] getReverseMap() {
        return didiMap.getMapReverse();
    }

    @Override
    public void reverseMap() {
        boolean[][] map = didiMap.getMap();
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] reverseMap = new boolean[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                reverseMap[row][col] = !map[row][col];
            }
        }
        didiMap.setMapReverse(reverseMap);
    }
}
