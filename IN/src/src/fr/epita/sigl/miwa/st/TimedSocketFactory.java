package src.fr.epita.sigl.miwa.st;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;


public class TimedSocketFactory extends RMISocketFactory {
	private final int timeout;

    /**
     * @param timeout The time out in milliseconds.
     */
    public TimedSocketFactory(final int timeout) {
        this.timeout = timeout;
    }

    public Socket createSocket(final String host, final int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), timeout);
        socket.setSoTimeout(timeout);
        socket.setSoLinger(false, 0);
        return socket;
    }

    public ServerSocket createServerSocket(final int port) throws IOException {
        return getDefaultSocketFactory().createServerSocket(port);
    }
}
