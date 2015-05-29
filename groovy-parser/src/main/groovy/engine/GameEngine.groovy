package engine

import model.Chat
import model.Game
import model.Message
import model.Round


class GameEngine {

    Game game = new Game()
    Chat chat = new Chat()
    def games = []

    int round = 1
    int lineNumber = 1


    public void addLine(String line) {
        def cleanLine = new String(line.getBytes("UTF-8"))

        // chat csay_all,  "say_team" " say " ,
        if (cleanLine.contains("say_team") || cleanLine.contains("\" say \"")) {
            def chats = line.substring(line.indexOf("say")).split("\"")
            if (!chats[1].startsWith("!")) {
                chat.messages.add(Message.parse(cleanLine))
                println ">>>>>> " + cleanLine
            }
        }

        // prepare for game to start
        if (cleanLine.contains("LIVE!")) {
            game.startLive()
        }

        // User "10:40:04: "Tony<16><BOT><>" connected, address """
         if(cleanLine.contains(" connected, address ")){
            println parseConnectedUser(line)
        }

        // user changed the team " 10:40:04: "Tony<16><BOT>" switched from team <Unassigned> to <CT>"
        if (cleanLine.contains("switched from team")) {

        }

        // notice given, check if actual game started
        if (game.isLive()) {
            // game start
            if (cleanLine.contains("World triggered \"Restart_Round_")) {
                game.startGame()
                println lineNumber + ":  --- GAME STARTED:" + cleanLine
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
                println lineNumber + ":Round start:" + cleanLine
            }
            /*if (cleanLine.contains("World triggered \"Round_End\"")) {
                if (game.isRoundRunning()) {
                    game.endRound()
                    println lineNumber + ":Round Ended:" + cleanLine
                }
            } */

            // check if round ended
            if (cleanLine.contains("SFUI_Notice")) {
                game.endRound(getGameEndStatus(cleanLine))
                println lineNumber + "Round ended " + line

                // check if game ended
                def scores = getScores(line)
                if (scores.find({ team -> team.points == 16 })) {
                    println lineNumber + " --- GAME ended with points " + scores
                    game.endGame()
                    games.add(game)
                }
            }
        }

        lineNumber++
    }

    public def getScores(String line) {
        def words = line.replaceAll("\"","").replaceAll("\\(","").replaceAll("\\)","").split(" ")
        def teams = words.drop(words.findIndexOf {it.startsWith("SFUI_Notice")} + 1)

        return [toScores(teams[0],teams[1]), toScores(teams[2],teams[3])]
    }

    public def toScores(team, scores) {
        return [team: team, points: Integer.parseInt(scores.replaceAll("\"", ""))]
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


