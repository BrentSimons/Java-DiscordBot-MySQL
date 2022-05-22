-- drop table users;
-- drop schema discord;

create schema discord;
use discord;

create table Users
(
    discordID         bigint primary key,
    discordUsername   varchar(128) not null,
    totalSentMessages int          not null,
    serverLevel       int          not null,
    serverXp          int          not null
);

# --------------------------------------------------------------

INSERT INTO discord.users
(discordID,
 discordUsername,
 totalSentMessages,
 serverLevel,
 serverXp)
VALUES (259751295856697856, "Yarin", 10, 1, 0);

INSERT INTO discord.users
(discordID,
 discordUsername,
 totalSentMessages,
 serverLevel,
 serverXp)
VALUES (269691295856697856, "Brent", 35, 3, 0);