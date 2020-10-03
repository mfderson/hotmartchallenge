alter table product add column category_id bigint, add constraint fk_product_category foreign key (category_id) references category(id);
