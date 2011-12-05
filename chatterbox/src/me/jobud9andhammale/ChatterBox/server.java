package me.jobud9andhammale.ChatterBox;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class server implements Runnable{

    public final main plugin;

	public server(main plugin) {
		this.plugin = plugin;
	}

	public void run() {
		int port = plugin.config.getInt("webserver.port");
		boolean debugmode = plugin.config.getBool("webserver.debug_mode");
		
		String request = plugin.PluginDirPath +"html"+File.separator;
     	ServerSocket s;
     	
        try {
            s = new ServerSocket(port);
        } catch (Exception e) {
            plugin.toConsole("Cannot bind to port "+port+"! Port is in use!", 3);
            return;
        }
        for (;;) {
            try {
                Socket remote = s.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        remote.getInputStream()));
                PrintWriter out = new PrintWriter(remote.getOutputStream());
            	//	InetAddress IP = remote.getLocalAddress();
            	//	plugin.toConsole(IP+" is connecting to webserver", 1);
                String str = ".";
                while (!str.equals(""))
                    str = in.readLine();
                //////////////////Connection heading//////////////////////
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("Server: Chatterbox");
                out.println("Connection: Close");
                out.println("");
                //////////////////Connection heading//////////////////////
                
                if(debugmode){
                	out.println("debug_mode active");
                }
                
                FileInputStream fstream = new FileInputStream(request+"index.htm");
                DataInputStream input = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                	out.println(strLine);
                }
                input.close();
                out.flush();
                remote.close();
                
            } catch (Exception e) {
            	if(debugmode){
            		plugin.toConsole("Error: " + e, 3);
            	}
            }
        }
	}
}