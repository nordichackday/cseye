package model

import org.apache.commons.lang3.builder.ToStringBuilder
import org.codehaus.groovy.syntax.Numbers

class Player {



    int id
    String name
    int kills = 0
    int deaths = 0
    int assists = 0
    int points = 0
    int headshots =  0
    int defuse = 0
    int bomb = 0
    String side

    @Override
    String toString() {
        return ToStringBuilder.reflectionToString(this)
    }

    static Player parsePlayer(String line){
        def list =  line.replaceAll("><","|").replaceAll("<","|").replaceAll(">","|").split("\\|")
        def side = (list[3].toLowerCase().equals("terrorist") ? "t":list[3].toLowerCase())
        Player player = new Player(name: list[0],id: Numbers.parseInteger(list[1],),side: side)
        return player
    }
}
