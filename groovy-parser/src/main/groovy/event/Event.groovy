package event

/**
 * Created by n23803 on 29.05.15.
 */
abstract class Event {

	def type

	Event(type) {
		this.type = type
	}

	public static parseTime(chatLine) {
		def timeRegex = /(.*): /
		def time = (chatLine =~ timeRegex)

		return time[0][1]
	}

	public static parseId(chatLine) {
		def idRegex = /.*<(\d+)>.*"/
		def id = (chatLine =~ idRegex)

		return id[0][1]
	}




}
