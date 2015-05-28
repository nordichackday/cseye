package app;

import endpoint.UpdServer;
import endpoint.WebAPI;
import engine.GameEngine;

public class Application {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine()
        WebAPI webAPI = new WebAPI(engine);
        UpdServer upd = new UpdServer(engine);
    }

}

