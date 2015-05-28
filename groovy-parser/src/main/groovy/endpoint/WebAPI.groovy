package endpoint

import engine.GameEngine
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import spark.Filter
import spark.Request
import spark.Response
import spark.Route

import static spark.Spark.*

/** end point for the web interface */
class WebAPI {


	public WebAPI(GameEngine engine) {

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
		def inputFile = this.getClass().getResource("/response.json")
		def json = new JsonSlurper().parseText(inputFile.text)
		json.id = matchId;
		return JsonOutput.toJson(json)
	}



}
