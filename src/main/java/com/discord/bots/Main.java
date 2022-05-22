package com.discord.bots;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.interaction.SlashCommand;
import org.json.JSONObject;


import javax.xml.transform.Result;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Random;

public class Main {
    public static String prefix = "$$";

    public static void main(String[] args) {
        // inloggen op discord
        DiscordApi api = new DiscordApiBuilder()
                .setToken("discord-bot-token")
                .login().join();

        // command dat alle commands in een embed zet
        api.addMessageCreateListener(eventShCommands -> {
            if (eventShCommands.getMessageContent().trim().equalsIgnoreCase(prefix + "commands")) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Commands!")
                        .setDescription("A short list of all commands that this bot adds")
                        .addInlineField("Ping", "Replies with pong")
                        .addInlineField("Copy text", "Replies with a copy of the text you wrote after copy")
                        .addInlineField("Name", "Replies with your name")
                        .addInlineField("ID", "Replies with your ID")
                        .addInlineField("Level", "Replies with your Discord level")
                        .addInlineField("Commands", "Shows the list of commands you are currently looking at");
                eventShCommands.getChannel().sendMessage(embed);
            }
        });

        // checkt elk bericht
        api.addMessageCreateListener(event -> {
            // pak standaard gegevents uit bericht
            MessageAuthor eventMA = event.getMessageAuthor();
            long discordID = eventMA.getId();
            String discordName = eventMA.getName();

            System.out.println("Initializing user " + discordName + " with ID " + discordID);
            try {
                // connect met de DB
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Discord", "username", "password");
                System.out.println(discordID + " connection to DB initialized\n");

                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE discordID='" + discordID + "'");

                System.out.println("Checking if user is in DB or not: ");

                // checkt of user die bericht stuurt in DB zit
                // result.next is negatief als die resultset leeg is
                if (!resultSet.next()) {
                    // voer user aan DB toe als negatief
                    System.out.print("!! User not in DB, adding!! ");
                    // je krijgt 50 xp voor je eerste bericht
                    statement.execute("INSERT INTO users (discordID,discordUsername,totalSentMessages, serverLevel,serverXp) VALUES ("
                            + discordID + ", '" + discordName + "',1,0,50);");
                } else {
                    // nieuwe user toevoegen
                    System.out.println("!! User in db !!");
                    // oude gegevens
                    int oldXp = resultSet.getInt(5);
                    int level = (resultSet.getInt(4));
                    Random rand = new Random();
                    // nieuwe xp berekenen
                    int wonXp;
                    int messageLength = event.getMessageContent().length();
                    if (level < 75) {
                        wonXp = rand.nextInt(messageLength + 100 - level);
                    } else {
                        wonXp = rand.nextInt(messageLength + 25);
                    }
                    // checken of de user genoeg xp heeft voor levelup
                    if (oldXp + wonXp > 1000) {
                        statement.execute("UPDATE users "
                                + "SET totalSentMessages = totalSentMessages + 1"
                                + ", serverLevel = serverLevel + 1  "
                                + ", serverXp = '" + 0 + "'"
                                + "WHERE discordID = '" + discordID + "'");
                        event.getChannel().sendMessage("<@" + discordID + "> has leveled up to level " + (level + 1));
                    } else {
                        System.out.println("Increasing messagecount (2) and xp");
                        statement.execute("UPDATE users "
                                + "SET totalSentMessages = totalSentMessages + 1"
                                + ", serverXp = serverXp + '" + wonXp + "'"
                                + "WHERE discordID = '" + discordID + "'");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // id finder command
        api.addMessageCreateListener(eventCheckID -> {
            if (eventCheckID.getMessageContent().trim().equalsIgnoreCase(prefix + "ID")) {
                MessageAuthor eventMessageAuthor = eventCheckID.getMessageAuthor();
                long discordID = eventMessageAuthor.getId();
                eventCheckID.getChannel().sendMessage(String.valueOf(discordID));
            }
        });

        // level checker command
        api.addMessageCreateListener(eventCheckLevel -> {
            if (eventCheckLevel.getMessageContent().trim().equalsIgnoreCase(prefix + "level")) {
                MessageAuthor eventMA = eventCheckLevel.getMessageAuthor();
                long discordID = eventMA.getId();
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Discord", "root", "1234");
                    Statement statement = con.createStatement();
                    ResultSet resultS = statement.executeQuery("SELECT serverLevel FROM users WHERE discordID=" + discordID);
                    if (resultS.next() == true) {
                        int level = (resultS.getInt(1));
                        eventCheckLevel.getChannel().sendMessage("User <@" + discordID + "> you are level " + level);
                    } else {
                        eventCheckLevel.getChannel().sendMessage("You are not in the DB");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // $$ping -> reply with: "Pong!"
        api.addMessageCreateListener(eventPing -> {
            if (eventPing.getMessageContent().equalsIgnoreCase(prefix + "ping")) {
                eventPing.getChannel().sendMessage("Pong!");
            }
        });

        // copy arguments
        api.addMessageCreateListener(eventCopy -> {
            if (eventCopy.getMessageContent().substring(0, 6).equalsIgnoreCase(prefix + "copy")) {
                eventCopy.getChannel().sendMessage(eventCopy.getMessageContent().substring(6));
                System.out.println("copy message tried\n");
            }
        });

        // replies with users name
        api.addMessageCreateListener(eventName -> {
            if (eventName.getMessageContent().substring(0, 6).equalsIgnoreCase(prefix + "name")) {
                eventName.getChannel().sendMessage(eventName.getMessageAuthor().getName());
            }
        });
    }
}