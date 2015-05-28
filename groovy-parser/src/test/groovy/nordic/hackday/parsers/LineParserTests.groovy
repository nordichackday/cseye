package nordic.hackday.parsers

import org.junit.Test

/**
 * Created by porijus on 28/05/15.
 */
class LineParserTests {


    @Test
    public void testParseSFUI_Notice_gameend(){
        String line = "12:48:35: Team \"TERRORIST\" triggered \"SFUI_Notice_Terrorists_Win\" (CT \"10\") (T \"16\")"
        def scores = line.substring(line.indexOf("("))
        def team1 = scores.substring(1,scores.indexOf(")")).split(" ")
        def team2 = scores.substring(scores.lastIndexOf("(") + 1,scores.length() - 1).split(" ")
        println toScores(team2)
        println toScores(team1)
    }

    private def toScores(String[] scores){
        return [team: scores[0], points: Integer.parseInt(scores[1].replaceAll("\"",""))]
    }

    @Test
    public void testParseSFUI_Notice_roundEndReason(){
        String line = "12:48:35: Team \"TERRORIST\" triggered \"SFUI_Notice_Terrorists_Win\" (CT \"10\") (T \"16\")"
        println line.split(" ")
        def msg = line.split(" ")[4].replaceAll("\"","")
        println msg
        println msg.split("_").drop(2).join("_").toLowerCase()

        String line2 = " 12:47:24: Team \"CT\" triggered \"SFUI_Notice_Bomb_Defused\" (CT \"10\") (T \"15\")"
        println line2.split(" ")
         msg = line2.split(" ")[4].replaceAll("\"","")
        println msg
        println msg.split("_").drop(2).join("_").toLowerCase()
    }

    @Test
    public void testParseUsers(){
        String line = " 10:40:04: \"Tony<16><BOT><>\" connected, address \"\""
        def user = line.trim().split("\"")
        def user2 =  user[1].replaceAll("<"," ").replaceAll(">"," ").split(" ")
        println user2[0]
        println user2[1]

    }

    @Test
    public void testChat(){
        String line = "\u000002/06/2015 - 10:59:22: \"Zunnu<3><STEAM_1:0:42115274><TERRORIST>\" say \"!r\""
        println line.substring(line.indexOf("say")).split("\"")

        line = "12:31:29: \"Jape1G<18><STEAM_1:0:36449907><TERRORIST>\" say_team \"VITTU TOI VARI\""
        println line.substring(line.indexOf("say")).split("\"")
    }
}
