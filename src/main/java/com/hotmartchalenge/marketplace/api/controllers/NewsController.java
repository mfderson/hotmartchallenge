package com.hotmartchalenge.marketplace.api.controllers;

import com.hotmartchalenge.marketplace.tasks.services.PopulateDatabaseService;
import com.hotmartchalenge.marketplace.tasks.services.TopHeadLinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {
  @Autowired private PopulateDatabaseService newsService;

  @Autowired private TopHeadLinesService top;

  @GetMapping("/populate")
  public void populate() {
    newsService.firstTimePopulateDb();
  }

  @GetMapping("/test")
  public void test() {
    top.topHeadlinesToPopulateDb();
  }
}
