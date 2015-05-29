package endpoint

import engine.GameEngine
import event.Frag
import groovy.json.JsonBuilder
import model.Game
import event.Message
import model.Player
import model.Round
import spark.Filter
import spark.Request
import spark.Response
import spark.Route

import static spark.Spark.*

/** end point for the web interface */
class WebAPI  {

	private GameEngine engine;

	public WebAPI(GameEngine engine) {

		this.engine = engine;

		staticFileLocation("/public");
		before new Filter() {
			void handle(Request request, Response response) throws Exception {
				response.type("application/json");
			}
		};

		get '/match/:matchId', new Route() {
			Object handle(Request request, Response response) throws Exception {

				def id = request.params(":matchId")
				if (id.equals("test")) {
					return getTestGame();
				} else {
					getMatch(id.toInteger())
				}
			}
		}
	}

	public Object getMatch(int matchId) {
		return new JsonBuilder(engine.game)
	}

	public Object getTestGame() {
		Game game = new Game()

		game.startGame();
		game.startRound(new Round(id: 1, started: true))
		game.events.add(new Message("JaakkoO", "goes skateboarding", "10:10"))
		game.events.add(new Frag(1, 2, "10:12"))

		return new JsonBuilder(game)
	}



}
