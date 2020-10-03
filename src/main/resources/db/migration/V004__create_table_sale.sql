create table sale(
  id          bigserial primary key,
  rating      real not null,
  created_at  timestamp(0) without time zone,
  product_id  bigint not null,
  constraint fk_sale_product foreign key (product_id) references product (id)
)
