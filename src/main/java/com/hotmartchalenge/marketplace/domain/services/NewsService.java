package com.hotmartchalenge.marketplace.domain.services;

import com.hotmartchalenge.marketplace.api.dtos.response.NewsResApiDto;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NewsService {
  @Autowired private NewsRepository newsRepository;

  @Autowired private CategoryRepository categoryRepository;

  private NewsResApiDto getNewsToPopulateDb(String category, String date) {
    WebClient client = WebClient.create("http://newsapi.org/v2");

    Mono<NewsResApiDto> monoNews =
        client
            .get()
            .uri(
                builder ->
                    builder
                        .path("/everything")
                        .queryParam("q", "bitcoin")
                        .queryParam("from", "2020-10-04")
                        .queryParam("apiKey", "22b3c63aa37348139c3b4fee6348939c")
                        .build())
            .retrieve()
            .bodyToMono(NewsResApiDto.class);

    NewsResApiDto news = monoNews.block();

    return news;
  }
}
