import groovy.json.JsonBuilder
import spark.Request
import spark.Response
import spark.Route

import static spark.Spark.*

class Service {


	public Service() {

		get '/match/:matchId', "application/json", new Route() {
			Object handle(Request request, Response response) throws Exception {
				return getMatch(request.params(":matchId").toInteger())
			}
		}
	}

	public Object getMatch(int matchId) {
		def builder = new JsonBuilder()

		def root = builder {
			id matchId
		}

		builder.toString();
	}


	public static void main(String[] args) {
		new Service();
	}
}
