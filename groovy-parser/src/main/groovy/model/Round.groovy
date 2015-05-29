package model

class Round {

    def started = false
    def id = -1
    String endStatus

    String winner
    String loser
    String ct
    String t


    @Override
    String toString() {
        return "Round " + id + " endStatus: " + endStatus + "\n"
    }
}
