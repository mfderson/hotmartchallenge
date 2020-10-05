create table news(
  id            bigserial primary key,
  published_at  timestamp,
  category_id   bigint not null,
  constraint fk_news_category foreign key (category_id) references category (id)
)
