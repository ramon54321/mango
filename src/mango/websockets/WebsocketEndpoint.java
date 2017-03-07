package mango.websockets;

import javax.websocket.*;
import javax.websocket.server.*;
import java.util.List;
import java.util.ArrayList;

@ServerEndpoint("/websocket")
public class WebsocketEndpoint {

	private static List<Session> userSessions = new ArrayList<Session>();

	private static void addSession(Session session) {
		userSessions.add(session);
	}

	private static void removeSession(Session session) {
		userSessions.remove(session);
	}

	public static void broadcast() {
		for (Session s : userSessions) {
			try {
				s.getBasicRemote().sendText("update_needed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connection opened.");
		addSession(session);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Connection closed.");
		removeSession(session);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Message: " + message);
	}
}
