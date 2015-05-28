/**
 * Created by porijus on 28/05/15.
 */
public class ParserDemo {


    public void run() {
        Game game = new Game()
        def games = []

        int round = 1
        int lineNumber = 1
        new File("/Users/porijus/devel/nordichackday/csgo_log_simulator/data/csgo-log").eachLine { line ->
            def cleanLine = new String(line.getBytes("UTF-8"))

            // prepare for game to start
            if (cleanLine.contains("LIVE!")) {
                game.live = true
            }

            // notice given, check if actual game started
            if (game.live) {
                // game start
                if (cleanLine.contains("World triggered \"Restart_Round_")) {
                    game.started = true
                    println lineNumber + ":  --- GAME STARTED:" + cleanLine
                }
            }
            // if someone reset game
            if (cleanLine.contains("Loading map")) {
                game = new Game()
            }
            // Game started, check rounds
            if (game.started) {
                if (cleanLine.contains("World triggered \"Round_Start\"")) {
                    game.rounds.add(new Round(id: round++, started: true))
                    println lineNumber + ":Round start:" + cleanLine
                }
                if (cleanLine.contains("World triggered \"Round_End\"")) {
                    if (game.rounds.last().started) {
                        game.rounds.last().started = false
                        println lineNumber + ":Round Ended:" + cleanLine
                    }
                }

                // check if round ended
                if (cleanLine.contains("SFUI_Notice")) {
                    if (game.rounds.last().started) {
                        game.rounds.last().started = false
                        println lineNumber + ":Round Ended:" + cleanLine
                    }
                    // check if game ended
                    def scores = getScores(line)
                    if(scores.find({team -> team.points == 16})){
                        println lineNumber + " --- GAME ended with points " + cleanLine
                        game.live = false
                        game.started = false
                        games.add(game)
                    }
                }
            }

            lineNumber++
        }

        print games


    }


    public static final void main(String[] args) {
        new ParserDemo().run()
    }

    private def getScores(String line){
        println line
        def scores = line.substring(line.indexOf("("))
        def team1 = scores.substring(1,scores.indexOf(")")).split(" ")
        def team2 = scores.substring(scores.lastIndexOf("(") + 1,scores.length() - 1).split(" ")
        return [toScores(team1),toScores(team2)]
    }

    private def toScores(String[] scores){
        return [team: scores[0], points: Integer.parseInt(scores[1].replaceAll("\"",""))]
    }
}



class Team {
    int id
    String name
    int points
    List<Player> players = []
}

class Player {
    int id
    String name
    int points
}


class Round {
    def started = false
    def id = -1

    def t
    def ct

    @Override
    String toString() {
        return "Round " + id + " status: " + started + "\n"
    }
}

class Game {
    def started = false
    def live = false
    List<Round> rounds = []

    Team team1
    Team team2

    @Override
    String toString() {
        return "GAME started:" + started + " live:" + live + " \n" + rounds.toListString()
    }
}
