
ALTER DATABASE bettingGround CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

drop table if exists test;
create table test (
                      id bigint not null auto_increment,
                      primary key (id)
) engine=InnoDB;

drop table if exists auction;
drop table if exists auction_image;
drop table if exists bid_history;
drop table if exists deal;
drop table if exists deal_event;
drop table if exists delivery;
drop table if exists payment;
drop table if exists settlement;
drop table if exists tag;
drop table if exists user;

create table auction (
                         id bigint not null auto_increment,
                         auction_status varchar(255),
                         bidder_id bigint,
                         content varchar(255),
                         created_at datetime(6),
                         current_price bigint,
                         duration varchar(255),
                         end_auction_time datetime(6),
                         instant_price bigint,
                         is_deleted bit not null,
                         item_condition varchar(255),
                         start_price bigint,
                         title varchar(255),
                         updated_at datetime(6),
                         view_cnt integer not null,
                         user_id bigint,
                         primary key (id)
) engine=InnoDB;
create table auction_image (
                               id bigint not null auto_increment,
                               image_url varchar(255),
                               thumbnail_url varchar(255),
                               auction_id bigint,
                               primary key (id)
) engine=InnoDB;
create table bid_history (
                             id bigint not null auto_increment,
                             bidder_id bigint,
                             created_at datetime(6),
                             nickname varchar(255),
                             price bigint,
                             auction_id bigint,
                             primary key (id)
) engine=InnoDB;
create table deal (
                      id bigint not null auto_increment,
                      buyer_id bigint,
                      deal_dead_line datetime(6),
                      deal_price bigint,
                      deal_status varchar(255),
                      deal_time datetime(6),
                      seller_id bigint,
                      auction_id bigint,
                      primary key (id)
) engine=InnoDB;
create table deal_event (
                            id bigint not null auto_increment,
                            created_at datetime(6),
                            deal_status varchar(255),
                            deal_id bigint,
                            primary key (id)
) engine=InnoDB;
create table delivery (
                          id bigint not null auto_increment,
                          delivery_company varchar(255),
                          invoice varchar(255),
                          auction_id bigint,
                          primary key (id)
) engine=InnoDB;
create table payment (
                         id bigint not null auto_increment,
                         created_at datetime(6),
                         money bigint,
                         payment_type varchar(255),
                         user_id bigint,
                         primary key (id)
) engine=InnoDB;
create table settlement (
                            id bigint not null auto_increment,
                            created_at datetime(6),
                            price bigint,
                            seller_id bigint,
                            primary key (id)
) engine=InnoDB;
create table tag (
                     id bigint not null auto_increment,
                     tag_name varchar(255),
                     auction_id bigint,
                     primary key (id)
) engine=InnoDB;
create table user (
                      id bigint not null auto_increment,
                      address_name varchar(255),
                      detail_address varchar(255),
                      road_name varchar(255),
                      zipcode integer,
                      bank_account varchar(255),
                      bank_name varchar(255),
                      created_at datetime(6),
                      email varchar(255),
                      is_deleted bit not null,
                      money bigint,
                      nickname varchar(255),
                      phone_number varchar(255),
                      profile_image varchar(255),
                      role varchar(255),
                      updated_at datetime(6),
                      username varchar(255),
                      primary key (id)
) engine=InnoDB;
