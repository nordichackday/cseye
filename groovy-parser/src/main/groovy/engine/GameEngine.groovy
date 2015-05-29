package engine

import event.Bomb
import event.Frag
import model.Chat
import model.Game
import event.Message
import model.Player
import model.Round
import model.Weapon
import spark.utils.StringUtils


class GameEngine {

    Game game = new Game()
    Chat chat = new Chat()
    def games = []

    int round = 1


    public void addLine(String line) {
		def cleanLine = line.getBytes("UTF-8")
        cleanLine = cleanLine[6..cleanLine.length-1]

        // chat csay_all,  "say_team" " say " ,
        if (cleanLine.contains("say_team") || cleanLine.contains("\" say \"")) {
            def chats = line.substring(line.indexOf("say")).split("\"")
            if (!chats[1].startsWith("!")) {
                game.events.add(Message.parse(cleanLine))
                println ">>>>>> " + cleanLine
            }
        }

        // prepare for game to start
        if (!game.live && cleanLine.contains("LIVE!")) {
            game = new Game()
            game.startLive()
        }

        // Purchased weapons
        if (cleanLine.contains(" purchased ")) {
            handlePurchase(line)
        }

        // user changed the team " 10:40:04: "Tony<16><BOT>" switched from team <Unassigned> to <CT>"
        // if (cleanLine.contains("switched from team")) {
        //
        //}
        // TODO handle switch team

        // notice given, check if actual game started
        if (game.isLive()) {
            // game start
            if (cleanLine.contains("World triggered \"Restart_Round_")) {

                game.startGame()
                println ":  --- GAME STARTED:" + cleanLine
                round = 1
            }
        }
        // if someone reset game
        if (cleanLine.contains("Loading map")) {
            game = new Game()
        }
        // Game started, check rounds
        if (game.isGameStarted()) {
            if (cleanLine.contains("World triggered \"Round_Start\"")) {
                game.startRound(new Round(id: round++, started: true))
                println ":Round start:" + cleanLine
            }

            if (cleanLine.contains(" killed ")) {
                def words = cleanLine.split("\"")
                Player killer = game.findOrCreatePlayer(Player.parsePlayer(words[1]))
                killer.kills += 1


                Player death = game.findOrCreatePlayer(Player.parsePlayer(words[3]))
                death.deaths += 1

                Weapon weapon = game.findOrCreateWeapon(words[words.findIndexOf { it.contains("with") } + 1])
                weapon.kills += 1

                if (words.find { it.contains("headshot") }) {
                    killer.headshots += 1
                    weapon.headshots += 1
                }

                game.events.add(Frag.parse(killer, death, weapon.name, cleanLine))
            }

            // check if round ended
            if (cleanLine.contains("SFUI_Notice")) {
                game.endRound(getGameEndStatus(cleanLine))
                println "Round ended " + line
                // TODO ADD winner and loser


                def scores = getScores(line)
                scores.each { sc ->
                    game.getTeam(sc.team).score = sc.points
                }

                // check if game ended
                if (scores.find({ team -> team.points == 16 })) {
                    println " --- GAME ended with points " + scores
                    game.endGame()

                }
            }

			if(cleanLine.contains("Planted_The_Bomb") || cleanLine.contains("Got_The_Bomb") || cleanLine.contains("Dropped_The_Bomb")) {
				game.events.add(Bomb.parse(cleanLine))
			}
        }

    }

    public void handlePurchase(String line) {
        def name = line.split("\"").last()
        if (StringUtils.isNotEmpty(name.trim())) {
            Weapon w = game.findOrCreateWeapon(name)
            w.bought += 1
        }
    }

    public def getScores(String line) {
        def words = line.replaceAll("\"", "").replaceAll("\\(", "").replaceAll("\\)", "").split(" ")
        def teams = words.drop(words.findIndexOf { it.startsWith("SFUI_Notice") } + 1)

        return [toScores(teams[0].toLowerCase(), teams[1]), toScores(teams[2].toLowerCase(), teams[3])]
    }

    public def toScores(team, scores) {
        try {
            scores = scores.replaceAll("\"", "")
            scores = scores.trim()
            return [team: team, points: Integer.parseInt(scores)]
        } catch (Exception ex) {
            System.err.println "Failing scores:'" + scores + "' "
            return [team: team, points: 1]
        }
    }

    public String getGameEndStatus(String line) {
        def words = line.replaceAll("\"", "").split(" ")
        def msg = words.find {
            it.startsWith("SFUI_Notice")
        }.replaceAll("\"", "")
        return msg.split("_").drop(2).join("_").toLowerCase()
    }

    public def parseConnectedUser(String line) {
        line = line.replaceAll("\n", "")
        def user = line.trim().split("\"")
        def user2 = user[1].replaceAll("<", " ").replaceAll(">", " ").split(" ")
        [user: user2[0], id: Integer.parseInt(user2[1]), scores: 0, kills: 0, deaths: 0]
    }
}


