package event

/**
 * Created by n23803 on 29.05.15.
 */
class Bomb extends Event {

	def text
	def id
	def time

	private Bomb(id, text, time) {
		super("bomb")

		this.time = time
		this.id = id
		this.text = text
	}

	static Bomb parse(chatLine) {
		def messageRegex = /.* triggered "(.*)"/
		def message = (chatLine =~ messageRegex)

		return new Bomb(parseId(chatLine), message[0][1], parseTime(chatLine))
	}
}
