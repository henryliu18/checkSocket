import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileInputStream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class checkSocket {

    public static void main(String[] args) {
        int exitStatus = 1 ;
        if (args.length != 1) {
            System.out.println("Usage: checkSocket INPUTFILE e.g server.csv");
        } else {
			String csvFile = "servers.csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
				try {
					br = new BufferedReader(new FileReader(csvFile));
					while ((line = br.readLine()) != null) {

                        // use comma as separator
                        String[] server = line.split(cvsSplitBy);

                        //System.out.println("Server = " + server[0]
                        //         + " , port=" + server[1] );
						
						String node = server[0];
						int port = Integer.parseInt(server[1]);
						int timeout = 2;
						
						Socket s = null;
						String reason = null ;
						try {
							s = new Socket();
							s.setReuseAddress(true);
							SocketAddress sa = new InetSocketAddress(node, port);
							s.connect(sa, timeout * 1000);
						} catch (IOException e) {
							if ( e.getMessage().equals("Connection refused")) {
								reason = "port " + port + " on " + node + " is closed.";
							};
							if ( e instanceof UnknownHostException ) {
								reason = "node " + node + " is not resolved, check DNS.";
							}
							if ( e instanceof SocketTimeoutException ) {
								reason = "timeout while attempting to reach node " + node + " on port " + port;
							}
						} finally {
							if (s != null) {
								if ( s.isConnected()) {
									System.out.println("Port " + port + " on " + node + " is reachable!");
									exitStatus = 0;
								} else {
									System.out.println("Port " + port + " on " + node + " is not reachable; reason: " + reason );
								}
								try {
									s.close();
								} catch (IOException e) {
								}
							}
						}
					}
				} catch (FileNotFoundException e) {
                e.printStackTrace();
				} catch (IOException e) {
						e.printStackTrace();
				} finally {
						if (br != null) {
								try {
										br.close();
								} catch (IOException e) {
										e.printStackTrace();
								}
						}
				}

        }
        System.exit(exitStatus);
    }
}

