package com.example.crawler.service;

import com.example.crawler.config.PlaywrightManager;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ：luke
 * @date ：Created in 2025/7/22 18:22
 * @description：
 * @modified By：
 */


@Service
public class XiaoHongShuScraper {

    @Autowired
    private PlaywrightManager playwrightManager;

    public List<Map<String, String>> scrapeNotes(String keyword, int maxPages) {
        List<Map<String, String>> notes = new ArrayList<>();
        BrowserContext context = playwrightManager.getContextWithSession();
        Page page = context.newPage();

        String url = "https://www.xiaohongshu.com/search_result?keyword=" + keyword;
        page.navigate(url);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        Set<String> seenIds = new HashSet<>();
        List<Map<String, String>> results = new ArrayList<>();
        int stableCount = 0;
        int maxStableCount = 3;

//        List<ElementHandle> items = page.querySelectorAll("section");
//        for (ElementHandle item : items) {
//            try {
//
//                ElementHandle titleEl = item.querySelector(".title");
//                ElementHandle nameEl = item.querySelector(".name");
//                ElementHandle timeEl = item.querySelector(".time");
//                String title = titleEl == null ? "" : titleEl.innerText();
//                String name = nameEl == null ? "" : nameEl.innerText();
//                String time = timeEl == null ? "" : timeEl.innerText();
//
//                Map<String, String> note = new HashMap<>();
//                note.put("title", title);
//                note.put("name", name);
//                note.put("time", time);
//                notes.add(note);
//            } catch (Exception e) {
//                System.err.println("⚠️ 解析单条数据失败: " + e.getMessage());
//            }
//        }

        Locator sections = page.locator("section.note-item");
        while (stableCount < maxStableCount) {
            int count = sections.count();
            boolean noNewDataThisRound = true;
            for (int i = 0; i < count; i++) {
                Locator sec = sections.nth(i);
                String dataIndex = sec.getAttribute("data-index");
                if (!seenIds.contains(dataIndex)) {
                    seenIds.add(dataIndex);
                    Locator titleLocator = sec.locator("span.title");
                    Locator nameLocator = sec.locator("span.name");
                    Locator timeLocator = sec.locator("span.time");
                    String title = titleLocator.count() > 0 ? titleLocator.innerText() : "";
                    String name = nameLocator.count() > 0 ? nameLocator.innerText() : "";
                    String time = timeLocator.count() > 0 ? timeLocator.innerText() : "";
                    Map<String, String> note = new HashMap<>();
                    note.put("dataIndex", dataIndex);
                    note.put("title", title);
                    note.put("name", name);
                    note.put("time", time);
                    results.add(note);
                    System.out.println("✅ 笔记：" + note);
                    noNewDataThisRound = false;
                }
            }
            if (noNewDataThisRound) {
                stableCount++;
            } else {
                stableCount = 0;
            }
            // 滚动到底部触发加载下一批数据
            sections.nth(count - 1).scrollIntoViewIfNeeded();
            page.waitForTimeout(1500);
        }
        System.out.println("✅ 已爬取数据，共 " + results.size() + " 条笔记。");
        System.out.println("记录：" + results);
        return results;
    }

}