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
}
