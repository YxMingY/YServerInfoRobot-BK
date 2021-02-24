package yxmingy.serverinforobot;

import org.bukkit.Bukkit;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.SocketException;

public class Robot extends Thread{
	private PrintWriter out;
	public boolean stop = false;
	private int port = 5700;
	public Robot(int port) {
		this.port = port;
	}
	public void run() {
		try {
			ServerSocket server = new ServerSocket(port);
			long heart_tick = 0;
			while(!stop) {
				Socket socket = server.accept();
				heart_tick = System.currentTimeMillis()/1000;
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
					//Output is automatically flushed by PrintWrite
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")), true);
					while (true) {
						//给cpu喘息的机会
						Thread.sleep(1000);
						//若心跳超时，则销毁socket，等待重新连接
						if(heart_tick + 12 < System.currentTimeMillis()/1000)
							break;
						String str = in.readLine();
						if ("cpdd".equals(str)) {
							out.println("dnmb");
							heart_tick = System.currentTimeMillis() / 1000;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (SocketException e){
					e.printStackTrace();
				} finally {
					info("小栗子端连接断开，等待重连...");
					socket.close();
				}
			}
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean sendGroupMsg(String m) {
		try {
			out.println("GM:"+m);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public boolean send(String m) {
		return sendGroupMsg(m.replaceAll("\\n","[n]"));
	}
	private static void info(String info) {
		Bukkit.getLogger().info(info);
	}
}
