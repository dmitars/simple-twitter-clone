create table user_subscriptons(
    channel_id bigint not null references usr,
    subscriber_id bigint not null references usr,
    primary key (channel_id,subscriber_id)
)