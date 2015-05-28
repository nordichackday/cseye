import model.Game
import model.Round

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
                game.startLive()
            }

            // User "10:40:04: "Tony<16><BOT><>" connected, address """
            // if(cleanLine.contains(" connected, address ")){
            //    println parseConnectedUser(line)
            //}


            // user changed the team " 10:40:04: "Tony<16><BOT>" switched from team <Unassigned> to <CT>"
            if(cleanLine.contains("switched from team")){

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

                    // check if game ended
                    def scores = getScores(line)
                    if (scores.find({ team -> team.points == 16 })) {
                        println lineNumber + " --- GAME ended with points " + cleanLine
                        game.endGame()
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

    private def getScores(String line) {
        def scores = line.substring(line.indexOf("("))
        def team1 = scores.substring(1, scores.indexOf(")")).split(" ")
        def team2 = scores.substring(scores.lastIndexOf("(") + 1, scores.length() - 1).split(" ")
        return [toScores(team1), toScores(team2)]
    }

    private def toScores(String[] scores) {
        return [team: scores[0], points: Integer.parseInt(scores[1].replaceAll("\"", ""))]
    }

    private String getGameEndStatus(String line) {
        def words = line.replaceAll("\"","").split(" ")
        def msg = words.find {
             it.startsWith("SFUI_Notice")
        }.replaceAll("\"", "")
        return msg.split("_").drop(2).join("_").toLowerCase()
    }

    private def parseConnectedUser(String line){
        line = line.replaceAll("\n","")
        println line
        def user = line.trim().split("\"")
        def user2 =  user[1].replaceAll("<"," ").replaceAll(">"," ").split(" ")
        println user2
        [user: user2[0], id: Integer.parseInt(user2[1])]
    }
}

