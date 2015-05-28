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
}
