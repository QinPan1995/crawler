package com.example.crawler.util;

import java.util.List;
import java.util.Random;

/**
 * @author ：luke
 * @date ：Created in 2025/7/22 18:30
 * @description：
 * @modified By：
 */


public class UserAgentUtil {
    private static final List<String> userAgents = List.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)",
            "Mozilla/5.0 (Linux; Android 10)"
    );

    public static String getRandomUserAgent() {
        Random random = new Random();
        return userAgents.get(random.nextInt(userAgents.size()));
    }
}
