package event

/**
 * Created by n23803 on 29.05.15.
 */
class Frag extends Event {

	def time
	def fragger
	def fragged

	Frag(fragger, fragged, time) {
		super("frag")

		this.fragger = fragger
		this.fragged = fragged
		this.time = time
	}

	public static Frag parse(fragger, fragged, chatLine) {
		def time = parseTime(chatLine)

		return new Frag(fragger.id, fragged.id, time)
	}
}
