package edu.tongji.vehicleroutingsim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description:
 * web测试控制器
 * 测试SpringBoot是否能正常运行
 * 测试地址：<a href="http://localhost:8080/test">...</a>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 5:50
 */
@Controller
public class WebTestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
