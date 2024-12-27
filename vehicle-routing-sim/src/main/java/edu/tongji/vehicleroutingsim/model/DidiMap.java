package edu.tongji.vehicleroutingsim.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

/**
 * Description:
 * DidiMap 类，用于存储滴滴地图信息。
 * <p>
 * 该类保存了地图的布尔二维数组表示，并提供了一些便捷的方法获取地图的行数和列数。
 * 每个单元格通过布尔值表示：
 * {@code true} 表示障碍物，{@code false} 表示空地。
 * </p>
 *
 * 主要功能：
 * <ul>
 *     <li>存储地图的二维布尔数组</li>
 *     <li>提供获取地图行数和列数的便捷方法</li>
 * </ul>
 *
 * 注意事项：
 * <ul>
 *     <li>该类实现了 {@link Serializable} 接口，因此支持序列化和反序列化。</li>
 *     <li>需要确保 {@code map} 数组在操作前已正确初始化。</li>
 * </ul>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 5:30
 */
@Component
@Data
public class DidiMap implements Serializable {

    @Serial
    private static final long serialVersionUID = -8674457569136527446L;

    /**
     * 地图的布尔二维数组表示
     */
    private boolean[][] map;

    private boolean[][] mapReverse;

}
