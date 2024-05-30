create table users
(
    id       LONG auto_increment,
    email    varchar not null,
    login    varchar not null,
    name     varchar,
    birthday date    not null,
    constraint users_pk
        primary key (id)
);
comment on column users.id is 'id пользователя';
comment on column users.email is 'email пользователя';
comment on column users.login is 'логин пользователя';
comment on column users.name is 'имя пользователя';
comment on column users.birthday is 'дата рождения пользователя';

create table friends
(
    user_id     LONG                  not null,
    friend_id   LONG                  not null,
    is_accepted BOOLEAN default false not null,
    constraint friend_fk
        foreign key (friend_id) references users (id)
            on update cascade on delete cascade,
    constraint user_fk
        foreign key (user_id) references users (id)
            on update cascade on delete cascade,
    constraint friends_pk
        primary key (user_id, friend_id)
);

comment on column friends.user_id is 'id пользователя';
comment on column friends.friend_id is 'id друга пользователя';
comment on column friends.is_accepted is 'принято ли предложение дружбы';

create table genres
(
    id   LONG auto_increment,
    name varchar not null,
    constraint genres_pk
        primary key (id)
);

comment on column genres.id is 'id жанра';
comment on column genres.name is 'Название жанра';

insert into genres (name)
values ('COMEDY'),
       ('DRAMA'),
       ('CARTOON'),
       ('THRILLER'),
       ('DOCUMENTARY'),
       ('ACTION');

create table mpa
(
    id   LONG auto_increment,
    name varchar not null,
    constraint mpa_pk
        primary key (id)
);
comment on column mpa.id is 'id рейтинга';
comment on column mpa.name is 'Название рейтинга';

insert into mpa (name)
values ('G'),
       ('PG'),
       ('PG_13'),
       ('R'),
       ('NC_17');

create table films
(
    id           LONG auto_increment,
    name         varchar not null,
    description  varchar not null,
    release_date date    not null,
    duration     LONG    not null,
    mpa_id       LONG,
    constraint films_pk
        primary key (id),
    constraint mpa_fk foreign key (mpa_id) references mpa (id)
);
comment on column films.id is 'id фильма';
comment on column films.name is 'Название фильма';
comment on column films.description is 'Описание фильма';
comment on column films.release_date is 'Дата выхода фильма';
comment on column films.duration is 'Продолжительность фильма';

create table films_genres
(
    film_id  long not null,
    genre_id long not null,
    constraint films_genres_FILMS_ID_fk
        foreign key (film_id) references FILMS
            on update cascade on delete cascade,
    constraint films_genres___fk
        foreign key (genre_id) references GENRES (id)
            on update cascade on delete cascade
);

comment on column films_genres.film_id is 'id фильма';
comment on column films_genres.genre_id is 'id жанра';

create table likes
(
    film_id LONG not null references films (id) on update cascade on delete cascade,
    user_id LONG not null references users (id) on update cascade on delete cascade,
    constraint likes_pk
        primary key (film_id, user_id)
);
comment on column likes.film_id is 'id фильма';
comment on column likes.user_id is 'id пользователя';

