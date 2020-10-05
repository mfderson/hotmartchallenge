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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NewsService {
  @Autowired private NewsRepository newsRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Value("${marketplace.news-api.base-url}")
  private String baseUrl;

  @Value("${marketplace.news-api.date-from}")
  private String dateFrom;

  @Value("${marketplace.news-api.apiKey}")
  private String apiKey;

  @Value("${marketplace.news-api.max-new-category-populate-db}")
  private int maxNumberNewsToInsert;

  private WebClient webClient;

  public void populateDb() {
    webClient = WebClient.create(baseUrl);
    List<Category> categories = categoryRepository.findAll();

    if (categories.isEmpty() || newsRepository.count() > 0) {
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
      news.setPublishedAt(OffsetDateTime.parse(article.getPublishedAt()));
      newsToInsert.add(news);
      if (newsToInsert.size() == maxNumberNewsToInsert) break;
    }

    newsRepository.saveAll(newsToInsert);
  }

  private NewsResApiDto getNewsToPopulateDb(String category) {
    Mono<NewsResApiDto> monoNews =
        webClient
            .get()
            .uri(
                builder ->
                    builder
                        .path("/everything")
                        .queryParam("q", category)
                        .queryParam("from", dateFrom)
                        .queryParam("apiKey", "22b3c63aa37348139c3b4fee6348939c")
                        .build())
            .retrieve()
            .bodyToMono(NewsResApiDto.class);

    NewsResApiDto news = monoNews.block();

    return news;
  }
}
