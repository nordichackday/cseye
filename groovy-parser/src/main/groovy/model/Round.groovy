package model

class Round {

    def started = false
    def id = -1
    String endStatus

    def t
    def ct

    @Override
    String toString() {
        return "Round " + id + " endStatus: " + endStatus + "\n"
    }
}
