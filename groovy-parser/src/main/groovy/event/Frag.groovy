package event

/**
 * Created by n23803 on 29.05.15.
 */
class Frag extends Event {

	def time
	def fragger
	def fragged
	def weapon

	Frag(fragger, fragged, weapon, time) {
		super("frag")

		this.fragger = fragger
		this.fragged = fragged
		this.time = time
		this.weapon = weapon
	}

	public static Frag parse(fragger, fragged, weapon, chatLine) {
		def time = parseTime(chatLine)

		return new Frag(fragger.id, fragged.id, weapon, time)
	}
}
