
-- t_stock

drop table if exists t_stock;

create table t_stock (
  f_id      bigint unsigned not null,
  f_ticker  varchar (50) not null,
  f_name    varchar (50)
)
engine = innodb;

alter table t_stock add constraint pk_stock primary key (f_id);
alter table t_stock add unique index ux_stock_1 (f_ticker);

-- t_stock_relation

drop table if exists t_stock_relation;

create table t_stock_relation (
  parent_id  bigint unsigned not null,
  child_id   bigint unsigned not null
)
engine = innodb;

alter table t_stock_relation add constraint pk_stock_relation primary key (parent_id, child_id);
alter table t_stock_relation add index ix_stock_relation_1 (parent_id);
alter table t_stock_relation add index ix_stock_relation_2 (child_id);
alter table t_stock_relation add constraint fk_stock_relation_1 foreign key (parent_id) references t_stock (f_id) on delete cascade;
alter table t_stock_relation add constraint fk_stock_relation_2 foreign key (child_id) references t_stock (f_id) on delete cascade;

-- t_price

drop table if exists t_price;

create table t_price (
  stock_id  bigint unsigned not null,
  f_date    date not null,
  f_value   double not null
)
engine = innodb;

alter table t_price add constraint pk_price primary key (stock_id, f_date);
alter table t_price add index ix_price_1 (stock_id);
alter table t_price add constraint fk_price_1 foreign key (stock_id) references t_stock (f_id) on delete cascade;

-- t_portfolio

drop table if exists t_portfolio;

create table t_portfolio (
  f_id          bigint unsigned not null,
  f_name        varchar (50) not null,
  f_date_start  date not null,
  indice_id     bigint unsigned not null
)
engine = innodb;

alter table t_portfolio add constraint pk_portfolio primary key (f_id);
alter table t_portfolio add index ix_portfolio_1 (indice_id);
alter table t_portfolio add constraint fk_portfolio_1 foreign key (indice_id) references t_stock (f_id);

-- t_holding

drop table if exists t_holding;

create table t_holding (
  portfolio_id  bigint unsigned not null,
  f_quantity    double not null,
  stock_id      bigint unsigned not null
)
engine = innodb;

alter table t_holding add constraint pk_holding primary key (portfolio_id, stock_id);
alter table t_holding add index ix_holding_1 (portfolio_id);
alter table t_holding add index ix_holding_2 (stock_id);
alter table t_holding add constraint fk_holding_1 foreign key (portfolio_id) references t_portfolio (f_id) on delete cascade;
alter table t_holding add constraint fk_holding_2 foreign key (stock_id) references t_stock (f_id);

