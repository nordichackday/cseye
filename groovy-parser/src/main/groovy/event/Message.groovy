package event

/**
 * Created by n23803 on 29.05.15.
 */
class Message extends Event {

	String name
	String text
	String time

	private Message(name, text, time) {
		this.name = name;
		this.text = text;
		this.time = time;
	}

	public static Message parse(chatLine) {
		def nameRegex = /.*"(.*)<\d+>/
		def matcher = ( chatLine =~ nameRegex )

		def timeRegex = /(.*): /
		def time = (chatLine =~ timeRegex)

		def messageRegex = /say "(.*)"/
		def message = (chatLine =~ messageRegex)

		return new Message(matcher[0][1], message[0][1], time[0][1])
	}

}
