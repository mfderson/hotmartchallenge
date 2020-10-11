package com.hotmartchalenge.marketplace.tasks.services;

import com.hotmartchalenge.marketplace.api.dtos.response.NewsResApiDto;
import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.News;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.NewsRepository;
import com.hotmartchalenge.marketplace.utils.FormatDatetimeUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PopulateDatabaseService {
  @Autowired private NewsRepository newsRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Value("${marketplace.news-api.base-url}")
  private String baseUrl;

  @Value("${marketplace.news-api.number-of-days-to-fill-database}")
  private int numberDaysFillDatabase;

  @Value("${marketplace.news-api.apiKey}")
  private String apiKey;

  @Value("${marketplace.news-api.max-new-category-populate-db}")
  private int maxNumberNewsToInsert;

  @Value("${marketplace.news-api.number-of-days-to-fill-database}")
  private int maxLastDaysToPopulateTableNews;

  private WebClient webClient;

  public void firstTimePopulateDb() {
    webClient = WebClient.create(baseUrl);
    List<Category> categories = categoryRepository.findAll();

    if (categories.isEmpty() || newsRepository.count() > 0) {
      return;
    }

    saveNewsFromLastDays(categories, numberDaysFillDatabase);
  }

  private void saveNewsFromLastDays(List<Category> categories, int lastDays) {
    LocalDate today = LocalDate.now();

    for (int i = 0; i < maxLastDaysToPopulateTableNews; i++) {
      LocalDate predecessor = today.minusDays(i);
      saveNews(categories, predecessor.toString(), predecessor.toString());
    }
  }

  public void saveNews(List<Category> categories, String dateFrom, String dateTo) {
    List<News> listNewsToInsert = new ArrayList<>();

    for (Category category : categories) {
      NewsResApiDto newResDto = getNewsFromApi(category.getName(), dateFrom, dateTo);
      News newsToInsert = new News();
      newsToInsert.setCategory(category);
      newsToInsert.setPublishedAt(FormatDatetimeUtils.convertTimeToStartDay(dateFrom));
      newsToInsert.setTotalResults(newResDto.getTotalResults());
      listNewsToInsert.add(newsToInsert);
      //   try {
      //     TimeUnit.SECONDS.sleep(5);
      //   } catch (InterruptedException e) {
      //     // TODO Auto-generated catch block
      //     e.printStackTrace();
      //   }
    }

    newsRepository.saveAll(listNewsToInsert);
  }

  // public void saveListNews(List<ArticlesResApiDto> newsList, Category category) {
  //   List<News> listNewsToInsert = new ArrayList<>();
  //   List<News> listDb =
  //       newsRepository.getAllBetweenDates(
  //           category.getId(),
  //           FormatDatetimeUtils.convertTimeToStartDay(dateFrom),
  //           FormatDatetimeUtils.convertTimeToStartDay(dateTo));

  //   System.out.println(":::listDb::: " + category.getName());
  //   System.out.println(listDb.size());

  //   for (ArticlesResApiDto article : newsList) {
  //     News news = new News();
  //     news.setCategory(category);
  //     news.setPublishedAt(OffsetDateTime.parse(article.getPublishedAt()));
  //     listNewsToInsert.add(news);
  //     // if (listNewsToInsert.size() == maxNumberNewsToInsert) break;
  //   }

  //   newsRepository.saveAll(listNewsToInsert);
  // }

  private NewsResApiDto getNewsFromApi(String category, String dateFrom, String dateTo) {
    Mono<NewsResApiDto> monoNews =
        webClient
            .get()
            .uri(
                builder ->
                    builder
                        .path("/everything")
                        .queryParam("q", category)
                        .queryParam("from", dateFrom)
                        .queryParam("to", dateTo)
                        .queryParam("apiKey", apiKey)
                        .build())
            .retrieve()
            .bodyToMono(NewsResApiDto.class);

    NewsResApiDto news = monoNews.block();

    return news;
  }
}
