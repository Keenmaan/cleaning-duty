# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table date (
  date                      date not null,
  locked                    boolean,
  work_day                  boolean,
  work_day_lock             boolean,
  worker_id                 bigint,
  holiday_id                bigint,
  constraint pk_date primary key (date))
;

create table holiday (
  id                        bigint not null,
  name                      varchar(255),
  day_of_the_year           integer,
  year                      integer,
  roster_name               varchar(255),
  constraint pk_holiday primary key (id))
;

create table leave (
  id                        bigint auto_increment not null,
  date                      date,
  worker_id                 bigint,
  constraint pk_leave primary key (id))
;

create table roster (
  name                      varchar(255) not null,
  in_use                    boolean,
  constraint pk_roster primary key (name))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  password                  varchar(255),
  is_admin                  boolean,
  worker_id                 bigint,
  constraint uq_user_name unique (name),
  constraint pk_user primary key (id))
;

create table worker (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  confirmed                 boolean,
  email                     varchar(255),
  user_id                   bigint,
  constraint uq_worker_email unique (email),
  constraint pk_worker primary key (id))
;

create sequence date_seq;

create sequence holiday_seq;

create sequence roster_seq;

create sequence user_seq;

alter table date add constraint fk_date_worker_1 foreign key (worker_id) references worker (id) on delete restrict on update restrict;
create index ix_date_worker_1 on date (worker_id);
alter table date add constraint fk_date_holiday_2 foreign key (holiday_id) references holiday (id) on delete restrict on update restrict;
create index ix_date_holiday_2 on date (holiday_id);
alter table holiday add constraint fk_holiday_roster_3 foreign key (roster_name) references roster (name) on delete restrict on update restrict;
create index ix_holiday_roster_3 on holiday (roster_name);
alter table leave add constraint fk_leave_worker_4 foreign key (worker_id) references worker (id) on delete restrict on update restrict;
create index ix_leave_worker_4 on leave (worker_id);
alter table user add constraint fk_user_worker_5 foreign key (worker_id) references worker (id) on delete restrict on update restrict;
create index ix_user_worker_5 on user (worker_id);
alter table worker add constraint fk_worker_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_worker_user_6 on worker (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists date;

drop table if exists holiday;

drop table if exists leave;

drop table if exists roster;

drop table if exists user;

drop table if exists worker;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists date_seq;

drop sequence if exists holiday_seq;

drop sequence if exists roster_seq;

drop sequence if exists user_seq;

