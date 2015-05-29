package event

import org.junit.Test

/**
 * Created by n23803 on 29.05.15.
 */
class BombTest {

	// 01/17/2015 - 12:29:10: "Boom-say<20><STEAM_1:1:37250067><TERRORIST>" triggered "Dropped_The_Bomb"
	@Test
	public void parseBomb() {
		def chatline = '01/17/2015 - 12:29:10: "Boom-say<20><STEAM_1:1:37250067><TERRORIST>" triggered "Dropped_The_Bomb"'

		def bomb = Bomb.parse(chatline)

		assert bomb.text == "Dropped_The_Bomb"
		assert bomb.id == 20
		assert bomb.time == "01/17/2015 - 12:29:10"
	}
}
