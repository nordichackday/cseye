package model

import event.Event

class Round {

	def started = false
	def id = -1
	String endStatus

	String winner
	String loser
	String ct
	String t
	List<Event> events = new ArrayList<>()


	@Override
	String toString() {
		return "Round " + id + " endStatus: " + endStatus + "\n"
	}
}
