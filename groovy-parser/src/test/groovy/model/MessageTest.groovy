package model

import org.junit.Test

/**
 * Created by n23803 on 29.05.15.
 */
class MessageTest {

	@Test
	void parseLineToChat(){
		String line = '01/17/2015 - 11:27:03: "Cyrn<3><STEAM_1:0:38750314><TERRORIST>" say "server fine?"'

		def message = Message.parse(line)

		assert message.name == "Cyrn"
		assert message.time == "01/17/2015 - 11:27:03"
		assert message.text == "server fine?"
	}
}
