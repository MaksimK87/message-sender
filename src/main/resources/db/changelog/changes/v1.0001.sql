CREATE SEQUENCE if not exists message_id_seq START WITH 1 INCREMENT BY 1;

create table if not exists messages
(
    id bigserial not null
        constraint table_name_pk
            primary key,
    status varchar(50),
    cpa_response_status varchar(50),
    create_date_time timestamp not null,
    update_date_time timestamp,
    event_type varchar(50) not null,
    phone varchar(30),
    locale varchar(10),
    ticket_title bigint,
    workplace_title varchar,
    ticket_id uuid
);

alter table messages owner to postgres;



