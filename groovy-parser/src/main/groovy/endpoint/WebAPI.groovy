package endpoint

import engine.GameEngine
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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
				return getMatch(request.params(":matchId").toInteger())
			}
		}
	}

	public Object getMatch(int matchId) {
		return new JsonBuilder(engine)
	}



}
