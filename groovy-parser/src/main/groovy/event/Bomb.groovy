package event

/**
 * Created by n23803 on 29.05.15.
 */
class Bomb extends Event {

	def text
	def player
	def time

	private Bomb(player, text, time) {
		super("bomb")

		this.time = time
		this.player = player
		this.text = text
	}

	static Bomb parse(chatLine) {

		def idRegex = /.*<(\d+)>.*"/
		def id = (chatLine =~ idRegex)

		def messageRegex = /.* triggered "(.*)"/
		def message = (chatLine =~ messageRegex)

		return new Bomb(id[0][1].toInteger(), message[0][1], parseTime(chatLine))
	}
}
