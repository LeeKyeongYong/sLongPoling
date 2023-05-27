package com.longpolling.chatting;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class MySessionListener
 *
 */
public class MessionLisener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public MessionLisener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    	HttpSession session = event.getSession();
    	String nickname = (String)session.getAttribute("nickname");
    	Messages messages = Messages.getInstance();
    	messages.removeNickname(nickname);
    }
}
