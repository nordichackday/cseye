package app;

import endpoint.UpdServer;
import endpoint.WebAPI;
import engine.GameEngine

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors;

public class Application {

    public static void main(String[] args) {

		GameEngine engine = new GameEngine()

		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(new UpdServer(engine))
		WebAPI webAPI = new WebAPI(engine);
    }

}

