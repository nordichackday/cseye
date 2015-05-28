/**
 * Created by porijus on 28/05/15.
 */
public class ParserDemo {



    public void run(){
        Game game = new Game();
        def round = 1
        new File("/Users/porijus/devel/nordichackday/csgo_log_simulator/data/csgo-log").eachLine {line ->
            def cleanLine = new String(line.getBytes("UTF-8"))

            if(cleanLine.contains("csay_version")){
                game.live = true
            }

            if(cleanLine.contains("eBot: LIVE!")){
                game.live = true
            }
            if(game.live){
                if(cleanLine.contains("World triggered \"Restart_Round_")){
                    game.started = true
                    println "GAME STARTED:" + cleanLine
                }
            }
            if(game.started){
                if(cleanLine.contains("World triggered \"Round_Start\"")){
                    game.rounds.add(new Round(id: round, true))
                }
                if(cleanLine.contains("World triggered \"Round_End\"")){
                    game.rounds.last().started  = false
                }
            }

        }

        print game


    }


    public static final void main(String[] args){
        new ParserDemo().run()
    }
}

class Round {
    def started = false
    def id = -1

    @Override
    String toString() {
        return "Round "+id+" status: " + started + "\n"
    }
}

class Game {
    def started = false
    def live = false
    List<Round> rounds = []

    @Override
    String toString() {
        return "GAME started:"+started+" live:"+live+" \n" + rounds.toListString()
    }
}
