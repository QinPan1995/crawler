package com.example.crawler.controller;

import com.example.crawler.config.PlaywrightManager;
import com.example.crawler.service.XiaoHongShuScraper;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：luke
 * @date ：Created in 2025/7/22 18:21
 * @description：
 * @modified By：
 */


@RestController
@RequestMapping("/api/scrape")
public class ScrapeController {

    @Autowired
    private XiaoHongShuScraper scraper;

    @Autowired
    private PlaywrightManager playwrightManager;

    @GetMapping("/login")
    public String login() {
        BrowserContext context = playwrightManager.getContextWithSession();
        Page page = context.newPage();
        page.navigate("https://www.xiaohongshu.com");

        // 检查是否存在登录弹窗
        boolean needLogin = page.isVisible("div[class*='login-modal']");
        if (needLogin) {
            System.out.println("⚠️ 检测到登录弹窗，请扫码登录...");
            page.waitForTimeout(60000); // 等待60秒扫码
            playwrightManager.saveSession(context);
            return "✅ 登录成功，会话已保存！";
        } else {
            return "✅ 已是登录状态，无需扫码。";
        }
    }
    @PostMapping("/notes")
    public List<Map<String, String>> scrapeNotes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int pages
    ) {
        return scraper.scrapeNotes(keyword, pages);
    }
}
