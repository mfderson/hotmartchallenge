package com.hotmartchalenge.marketplace.tasks.services;

import com.hotmartchalenge.marketplace.api.dtos.response.NewsResApiDto;
import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.News;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.NewsRepository;
import com.hotmartchalenge.marketplace.utils.FormatDatetimeUtils;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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

  public void topHeadlines() {
    webClient = WebClient.create(baseUrl);

    List<Category> categories = categoryRepository.findAll();
    List<News> listOfNewsToSave = new ArrayList<>();

    for (Category category : categories) {
      NewsResApiDto newResApiDto = getApiTopHeadlinesByCategory(category.getName());
      addNewsInList(newResApiDto, category, listOfNewsToSave);
    }

    newsRepository.saveAll(listOfNewsToSave);
  }

  public void addNewsInList(NewsResApiDto apiNews, Category category, List<News> listOfNewsToSave) {
    if (!existApiNews(apiNews)) {
      return;
    }

    OffsetDateTime startOfDay = FormatDatetimeUtils.convertTimeToStartOfDay(OffsetDateTime.now());
    OffsetDateTime finalOfDay = FormatDatetimeUtils.convertTimeToFinalOfDay(OffsetDateTime.now());

    List<News> newsListDb =
        newsRepository.getAllBetweenDates(category.getId(), startOfDay, finalOfDay);

    if (!newsListDb.isEmpty()) {
      Integer totalNews = newsListDb.get(0).getTotalResults() + apiNews.getTotalResults();
      newsListDb.get(0).setTotalResults(totalNews);
      listOfNewsToSave.add(newsListDb.get(0));
    } else {
      News newsToInsert = new News();
      newsToInsert.setCategory(category);
      newsToInsert.setPublishedAt(
          FormatDatetimeUtils.convertTimeToStartOfDay(OffsetDateTime.now()));
      newsToInsert.setTotalResults(apiNews.getTotalResults());
      listOfNewsToSave.add(newsToInsert);
    }
  }

  private boolean existApiNews(NewsResApiDto news) {
    return news.getTotalResults() > 0 ? true : false;
  }

  private NewsResApiDto getApiTopHeadlinesByCategory(String category) {
    Mono<NewsResApiDto> monoNews =
        webClient
            .get()
            .uri(
                builder ->
                    builder
                        .path("/top-headlines")
                        .queryParam("q", category)
                        .queryParam("pageSize", 1)
                        .queryParam("apiKey", apiKey)
                        .build())
            .retrieve()
            .bodyToMono(NewsResApiDto.class);

    NewsResApiDto news = monoNews.block();

    return news;
  }
}
