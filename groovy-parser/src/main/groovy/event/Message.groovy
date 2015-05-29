package event

/**
 * Created by n23803 on 29.05.15.
 */
class Message extends Event {

	String name
	String text
	String time

	private Message(name, text, time) {
		super("message")
		this.name = name;
		this.text = text;
		this.time = time;
	}

	public static Message parse(chatLine) {
		def nameRegex = /.*"(.*)<\d+>/
		def matcher = ( chatLine =~ nameRegex )

		def messageRegex = /say "(.*)"/
		def message = (chatLine =~ messageRegex)

		return new Message(matcher[0][1], message[0][1], parseTime(chatLine))
	}

}
