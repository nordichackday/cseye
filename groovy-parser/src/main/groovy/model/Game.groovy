package model

import event.Event

class Game {

    private def started = false
    private def live = false
    List<Round> rounds = []
	List<Event> events = []

    private Team team1 = new Team(side: "t")
    private Team team2 = new Team(side: "ct")



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
            round.loser = ""
            round.winner = ""

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
