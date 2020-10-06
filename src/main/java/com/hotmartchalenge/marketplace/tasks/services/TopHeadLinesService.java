package com.hotmartchalenge.marketplace.tasks.services;

import com.hotmartchalenge.marketplace.api.dtos.response.ArticlesResApiDto;
import com.hotmartchalenge.marketplace.api.dtos.response.NewsResApiDto;
import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.News;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.NewsRepository;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TopHeadLinesService {
  @Autowired private NewsRepository newsRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Value("${marketplace.news-api.base-url}")
  private String baseUrl;

  @Value("${marketplace.news-api.date-from}")
  private String dateFrom;

  @Value("${marketplace.news-api.apiKey}")
  private String apiKey;

  private WebClient webClient;

  public void testQueryMarota() {
    OffsetDateTime start =
        OffsetDateTime.parse("2020-10-05T00:00:00+00:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    OffsetDateTime end =
        OffsetDateTime.parse("2020-10-05T23:59:59+00:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    List<News> news = newsRepository.getAllBetweenDates(1L, start, end);
    System.out.println(":::Query marota:::");
    System.out.println(news.size());
  }

  public void topHeadlinesToPopulateDb() {
    webClient = WebClient.create(baseUrl);

    List<Category> categories = categoryRepository.findAll();

    for (Category category : categories) {
      NewsResApiDto newResDto = getTopHeadlinesToPopulateDb(category.getName());
      saveListNews(newResDto, category);
    }
  }

  public void saveListNews(NewsResApiDto news, Category category) {
    if (!existNews(news)) {
      return;
    }

    String earliestDate = getEarliestDateFromArticles(news.getArticles());
    OffsetDateTime startDate =
        OffsetDateTime.parse(
            String.format("%sT00:00:00+00:00", earliestDate),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    OffsetDateTime endDate =
        OffsetDateTime.parse(
            String.format("%sT23:59:59+00:00", earliestDate),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    List<News> newsListDb = newsRepository.getAllBetweenDates(category.getId(), startDate, endDate);
    System.out.println(
        String.format(
            ":::Category name: %s, newsListDb.size: ", category.getName(), newsListDb.size()));

    if (!newsListDb.isEmpty()) {
      System.out.println(
          String.format(
              "Category name: %s, News id: %d", category.getName(), newsListDb.get(0).getId()));
      System.out.println(newsListDb.get(0).getId());
      newsListDb.get(0).setTotalResults(news.getTotalResults());
      newsRepository.save(newsListDb.get(0));
    } else {
      News newsToInsert = new News();
      newsToInsert.setCategory(category);
      newsToInsert.setPublishedAt(startDate);
      newsToInsert.setTotalResults(news.getTotalResults());
      newsRepository.save(newsToInsert);
    }
  }

  private boolean existNews(NewsResApiDto news) {
    return news.getTotalResults() > 0 ? true : false;
  }

  private String getEarliestDateFromArticles(List<ArticlesResApiDto> articles) {
    return articles.get(0).getPublishedAt().split("T")[0];
  }

  private NewsResApiDto getTopHeadlinesToPopulateDb(String category) {
    Mono<NewsResApiDto> monoNews =
        webClient
            .get()
            .uri(
                builder ->
                    builder
                        .path("/top-headlines")
                        .queryParam("q", category)
                        .queryParam("country", "us")
                        .queryParam("apiKey", apiKey)
                        .build())
            .retrieve()
            .bodyToMono(NewsResApiDto.class);

    NewsResApiDto news = monoNews.block();

    return news;
  }
}