package model

class Team {

    String side
    int id
    String name
    int score
    List<Player> players = []

    Player findPlayer(int id){
        return players.find {it.id == id}
    }


}
