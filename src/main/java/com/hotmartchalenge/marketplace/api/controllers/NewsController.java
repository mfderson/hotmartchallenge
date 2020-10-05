package com.hotmartchalenge.marketplace.api.controllers;

import com.hotmartchalenge.marketplace.domain.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {
  @Autowired private NewsService newsService;

  @GetMapping("/populate")
  public void populate() {
    newsService.populateDb();
    System.out.println("Dados da primeira noticia:");
    // System.out.println(news.getStatus());
    // System.out.println(news.getTotalResults());
    // System.out.println(news.getArticles().get(0).getUrl());
    // System.out.println(news.get(0).getPublishedAt());
    // System.out.println(news.get(0).getTitle());
  }
}
