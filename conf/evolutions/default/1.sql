# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table date (
  date                      date not null,
  locked                    boolean,
  work_day                  boolean,
  work_day_lock             boolean,
  worker_id                 bigint,
  roster_id                 bigint,
  constraint pk_date primary key (date))
;

create table leave (
  id                        bigint auto_increment not null,
  date_start                date,
  date_end                  date,
  worker_id                 bigint,
  constraint pk_leave primary key (id))
;

create table roster (
  id                        bigint auto_increment not null,
  constraint pk_roster primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  is_admin                  boolean,
  worker_id                 bigint,
  constraint uq_user_name unique (name),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create table worker (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  constraint pk_worker primary key (id))
;

create sequence date_seq;

create sequence user_seq;

alter table date add constraint fk_date_worker_1 foreign key (worker_id) references worker (id) on delete restrict on update restrict;
create index ix_date_worker_1 on date (worker_id);
alter table date add constraint fk_date_roster_2 foreign key (roster_id) references roster (id) on delete restrict on update restrict;
create index ix_date_roster_2 on date (roster_id);
alter table leave add constraint fk_leave_worker_3 foreign key (worker_id) references worker (id) on delete restrict on update restrict;
create index ix_leave_worker_3 on leave (worker_id);
alter table user add constraint fk_user_worker_4 foreign key (worker_id) references worker (id) on delete restrict on update restrict;
create index ix_user_worker_4 on user (worker_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists date;

drop table if exists leave;

drop table if exists roster;

drop table if exists user;

drop table if exists worker;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists date_seq;

drop sequence if exists user_seq;

