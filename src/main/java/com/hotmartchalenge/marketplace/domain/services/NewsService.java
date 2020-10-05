package com.hotmartchalenge.marketplace.domain.services;

import com.hotmartchalenge.marketplace.api.dtos.response.ArticlesResApiDto;
import com.hotmartchalenge.marketplace.api.dtos.response.NewsResApiDto;
import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.News;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.NewsRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NewsService {
  @Autowired private NewsRepository newsRepository;

  @Autowired private CategoryRepository categoryRepository;

  private static final WebClient WEB_CLIENT = WebClient.create("http://newsapi.org/v2");

  private static final String DATE_FROM = "2020-10-01";

  private static final int MAX_NUM_NEWS_TO_INSERT = 20;

  public void populateDb() {
    List<Category> categories = categoryRepository.findAll();
    if (categories.isEmpty()) {
      return;
    }

    for (Category category : categories) {
      NewsResApiDto newResDto = getNewsToPopulateDb(category.getName());
      saveListNews(newResDto.getArticles(), category);
    }
  }

  public void saveListNews(List<ArticlesResApiDto> newsList, Category category) {
    List<News> newsToInsert = new ArrayList<>();
    for (ArticlesResApiDto article : newsList) {
      News news = new News();
      news.setCategory(category);
      System.out.println(article.getPublishedAt());
      news.setPublishedAt(OffsetDateTime.parse(article.getPublishedAt()));
      newsToInsert.add(news);
      if (newsToInsert.size() == MAX_NUM_NEWS_TO_INSERT) break;
    }

    newsRepository.saveAll(newsToInsert);
  }

  private NewsResApiDto getNewsToPopulateDb(String category) {

    Mono<NewsResApiDto> monoNews =
        WEB_CLIENT
            .get()
            .uri(
                builder ->
                    builder
                        .path("/everything")
                        .queryParam("q", category)
                        .queryParam("from", DATE_FROM)
                        .queryParam("apiKey", "22b3c63aa37348139c3b4fee6348939c")
                        .build())
            .retrieve()
            .bodyToMono(NewsResApiDto.class);

    NewsResApiDto news = monoNews.block();

    return news;
  }
}
