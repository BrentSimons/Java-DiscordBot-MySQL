# PDP Project Java Discord Bot

In deze repository kan je het project vinden dat ik heb gemaakt voor mijn vak Professional Skills 1.

## Algemene info:

In deze repository kan je een Java Discord Bot vinden die ook voor school heb gemaakt. De functies van de bot zijn:

- Levels geven op basis van aantal gestuurde berichten
  - Deze levels worden opslaan in een database 
- Enkele commandos zoals:
  - $$level: de bot reageert met je level.
  - $$name: de bot reageert met je discordnaam.
  - $$id: de bot reageert met je discord ID.
  - $$commands: een discord embed die al de commands laat zien.
  - $$copy: de bot kopieert de argumenten die je achter de command plaatst.
  - $$ping: de bot reageert met pong.

## Gebruikte technologieën:

**[Discord Developer Portal](https://discord.com/developers/)**

Dit wordt gebruikt voor het aanmaken van de bot in Discord, ook kan je hier de naam, de beschrijving en de profielfoto van de Bot bewerken.

**[Java](https://www.java.com/)**

Dit is de taal waarin de bot is geschreven.

**[Gradle](https://gradle.org)**

Gebruikt voor het automatiseren van het downloaden, updaten van packages die in de code gebruikt worden.

**[Javacord](https://javacord.org/)**

De API gebruikt voor het maken van discord bots in Java.

**[JDBC](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)**

Java Database Connectivity: gebruikt voor een connectie te maken met een database, om queries te sturen.

**[MySQL](https://www.mysql.com/)**

De gebruikte database.


## Hoe werkt de database?

De database heeft 1 tabel met 5 kolommen, hierin worden de gegevens van de gebruikers binnen de discord server opgeslaan. De data die we opslaan per gebruiker zijn:

- discord ID (primary key)
- discord username
- aantal gestuurde berichten
- level
- xp sinds vorige level-up

Voor een connectie tussen de bot en de Database te maken is er gebruikgemaakt van JDBC.
## Hoe start je zelf de bot?

1. Maak een discord applicatie en bot aan op de [Discord Developer Portal](https://discord.com/developers/)
2. Maak een [MySQL server](https://dev.mysql.com/downloads/mysql/) aan of gebruik [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
3. Verander het discord bot token en de login gegevens van de database in de applicatie naar je eigen gegevens.
4. Run het build.gradle bestand zodat je de gebruikte packages installeert.
5. Run de applicatie.

## Hoe werken de levels

Voor elk level moet je 1000 xp krijgen, de xp wordt gegeven per bericht dat je stuurd en wordt berekent met de volgende formule:

    Randint(messageLength + (100 - level))

Maar als je level 75 of hoger bent, dan wordt de formule versimpelt tot:

    Randint(messageLength + 25)

Randint: 
Deze functie genereert een willekeurig integer tussen 0 en het getal dat je meegeeft
messageLength, level:
Deze variabelen bevatten de lengte van het gestuurde bericht en het level dat de gebruiker is.

Bij een level-up wordt je xp gereset naar 0.