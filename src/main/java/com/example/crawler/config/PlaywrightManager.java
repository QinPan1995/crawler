package com.example.crawler.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * @author ：luke
 * @date ：Created in 2025/7/23 18:03
 * @description：
 * @modified By：
 */


@Component
public class PlaywrightManager {
    private static final String SESSION_FILE = "xiaohongshu-session.json";

    private final Playwright playwright;
    private final Browser browser;

    public PlaywrightManager() {
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    public BrowserContext getContextWithSession() {
        Browser.NewContextOptions options = new Browser.NewContextOptions();
        if (Paths.get(SESSION_FILE).toFile().exists()) {
            options.setStorageStatePath(Paths.get(SESSION_FILE));
            System.out.println("✅ 已加载会话文件: " + SESSION_FILE);
        }
        return browser.newContext(options);
    }

    public void saveSession(BrowserContext context) {
        context.storageState(new BrowserContext.StorageStateOptions()
                .setPath(Paths.get(SESSION_FILE))
        );
        System.out.println("✅ 会话已保存: " + SESSION_FILE);
    }
}