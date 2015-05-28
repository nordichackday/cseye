package model

class Game {

    private def started = false
    private def live = false
    private List<Round> rounds = []

    private Team team1 = new Team(status: "TERRORIST")
    private Team team2 = new Team(status: "CT")



    void startGame() { started = true }
    void startLive(){live = true}

    void endGame() { started = false; live = false }

    boolean isLive() { live }

    boolean isGameStarted() { started }

    void startRound(Round round) {
        round.started = true
        rounds.add(round)
    }

    void endRound(String status) {
        def round = getRunningRound()
        if (round.started) {
            round.endStatus = status
            round.started = false
        }
    }

    boolean isRoundRunning(){
        rounds.last().started
    }

    Round getRunningRound() {
        if (rounds.last().started) {
            rounds.last()
        } else {
            return null
        }
    }


    @Override
    String toString() {
        return "GAME started:" + started + " live:" + live + " \n" + rounds.toListString()
    }
}
