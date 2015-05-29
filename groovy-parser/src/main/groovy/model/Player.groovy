package model

import org.apache.commons.lang3.builder.ToStringBuilder

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

    @Override
    String toString() {
        return ToStringBuilder.reflectionToString(this)
    }
}
