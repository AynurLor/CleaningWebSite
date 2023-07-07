create type request_state as enum(
    'CREATED',
    'APPROVED',
    'COMPLETED',
    'FAILED'
);

create table orderClient (
    id serial primary key,
    status text not null default 'CREATED',
    name text,
    numberPhone text not null,
    amount numeric(10,2),
    orderDate date default current_date
);

create table UserTelegram
(
    id serial primary key,
    idTelegram int,
    firstName text,
    lastName text,
    userName text
);

