package event

/**
 * Created by n23803 on 29.05.15.
 */
class Message extends Event {

	int id
	String text
	String time

	private Message(id, text, time) {
		super("message")
		this.id = id
		this.text = text
		this.time = time
	}

	public static Message parse(chatLine) {
		def messageRegex = /[say|say_team] "(.*)"/
		def message = (chatLine =~ messageRegex)

		return new Message(parseId(chatLine), message[0][1], parseTime(chatLine))
	}

}
