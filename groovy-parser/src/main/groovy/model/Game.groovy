package model

import event.Event

class Game {

    private def started = false
    private def live = false
    List<Round> rounds = []
	List<Event> events = []
    List<Weapon> weapons = []

    private Team team1 = new Team(side: "t")
    private Team team2 = new Team(side: "ct")


    void startGame() { started = true }

    void startLive() { live = true }

    void endGame() { started = false; live = false }

    boolean isLive() { live }

    boolean isGameStarted() { started }

    void startRound(Round round) {
        round.started = true
        rounds.add(round)
    }

    Player findOrCreatePlayer(Player player) {
        Player p = team1.findPlayer(player.id)
        if (!p)
            p = team2.findPlayer(player.id)

        if (!p) {
            if (team1.side.equals(player.side)) {
                team1.players.add(player)
                p = player
            } else if (team2.side.equals(player.side)) {
                team2.players.add(player)
                p = player
            }
        }
        return p
    }

    Weapon findOrCreateWeapon(String name) {
        Weapon w = weapons.find { it.name.equals(name) }
        if (!w) {
            w = new Weapon(name: name)
            weapons.add(w)
        }
        return w
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

    boolean isRoundRunning() {
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
