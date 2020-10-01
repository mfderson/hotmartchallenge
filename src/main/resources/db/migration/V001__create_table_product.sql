create table product(
  id          bigserial primary key,
  name        varchar(255) not null,
  description varchar(2048) not null,
  created_at  timestamp(0) without time zone
)
