package endpoint

import engine.GameEngine

/**
 * Created by porijus on 29/05/15.
 */
class UpdServer {

    public UpdServer(GameEngine engine){

        def socket = new DatagramSocket(3000)
        def buffer = (' ' * 4096) as byte[]
        while(true) {
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length)
            socket.receive(incoming)
            String line = new String(incoming.data, 0, incoming.length)
            engine.addLine(line)
            String reply = "Client said: 'thanks'"
            DatagramPacket outgoing = new DatagramPacket(reply.bytes, reply.size(),
                    incoming.address, incoming.port);
            socket.send(outgoing)
        }

    }

    public static final void main(String[] args){
        new UpdServer()
    }
}
