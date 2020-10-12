package com.hotmartchalenge.marketplace.tasks;

import com.hotmartchalenge.marketplace.tasks.services.TopHeadLinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class DownloadLastNewsTask {
  @Autowired private TopHeadLinesService topHeadLinesService;

  @Scheduled(cron = "0 0 1/6 * * *")
  public void downloadNews() {
    topHeadLinesService.topHeadlines();
  }
}
