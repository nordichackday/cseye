package model

import org.apache.commons.lang3.builder.ToStringBuilder

class Weapon {

    String name = ""
    int kills = 0
    int headshots = 0
    int bought = 0


    @Override
    String toString() {
        return ToStringBuilder.reflectionToString(this)
    }
}
