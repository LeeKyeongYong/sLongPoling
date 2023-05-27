package com.longpolling.chatting;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class ControllerServlet
 */
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Messages messages = Messages.getInstance();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String contextPath = this.getServletContext().getContextPath();
		String url = request.getRequestURI().substring(contextPath.length());

		if(url.equals("/index.do")) {
			String error = request.getParameter("error");
			
			// 닉네임 중복 오류 메시지 출력
			if(error != null && error.equals("nickname")) {
				request.setAttribute("error", "이미 사용 중인 닉네임입니다. 다른 닉네임을 입력해주세요.");
			}
			
			// 세션 오류 메시지 출력
			if(error != null && error.equals("session")) {
				request.setAttribute("error", "이미 접속한 세션이 존재합니다. 먼저 로그아웃해주세요.");
			}
			
			// 첫 화면 표시
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
			
		} else if(url.equals("/getMessages.do")) {
			// 세션에서 닉네임을 가져옴
			HttpSession session = request.getSession();
			String nickname = (String)session.getAttribute("nickname");
			String json = "[]";
			
			//System.out.println(nickname + "님의 메시지 요청 : " + new Date());
			
			if(nickname == null) {
				// 로그인하지 않은 사용자의 요청
				json = "{ \"result\" : \"FAILURE\" }";
				
			} else {
				Gson gson = new Gson();
				
				for(int i=0; i<100; i++) {	 // 최대 100번의 요청을 반복하여 확인
					synchronized(messages) { // 메시지를 확인하고, 새로운 메시지가 추가되지 않도록 동기화
						List<String> myMessages = messages.getMyMessages(nickname);
						
						// 세션에 해당하는 메시지가 삭제되었을 경우 종료
						if(myMessages == null) {
							return;
						}
						
						if(myMessages.size() > 0) { // 새로운 메시지가 추가되었으면
							// JSON 형식으로 변환하여 전송
							json = gson.toJson(myMessages);
							messages.removeMyMessage(nickname);
							break;
						} 
					}
					try {
						Thread.sleep(500);	// 0.5초 대기
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						throw new ServletException(e.getMessage());
					}
				}
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			writer.write(json);
			writer.flush();
			writer.close();
			
		} else if(url.equals("/exit.do")) {
			HttpSession session = request.getSession();
			String nickname = (String)session.getAttribute("nickname");

			if(nickname != null) {
				session.invalidate();
			}
			response.sendRedirect("index.do");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String contextPath = this.getServletContext().getContextPath();
		String url = request.getRequestURI().substring(contextPath.length());
		request.setCharacterEncoding("utf-8");
		
		if(url.equals("/enterRoom.do")) {
			// 이미 사용 중인 닉네임인지 확인 후 처리...
			String nickname = request.getParameter("nickname");
			
			if(messages.existNickname(nickname)) {
				response.sendRedirect("index.do?error=nickname");
				
			} else if(request.getSession().getAttribute("nickname") != null) {
				response.sendRedirect("index.do?error=session");
				
			} else {
				// 새로운 세션을 생성
				HttpSession session = request.getSession();
				session.setAttribute("nickname", nickname);
				session.setMaxInactiveInterval(60); // 60초 동안 요청이 없을 경우 세션 만료 처리.
			
				// 닉네임을 채팅방에 추가
				messages.addNickname(nickname);

				// 채팅 화면으로 이동
				RequestDispatcher dispatcher = request.getRequestDispatcher("chatting.jsp");
				dispatcher.forward(request, response);
			}
			
			} else if(url.equals("/sendMessage.do")) {

			String result = null;
			HttpSession session = request.getSession();
			String nickname = (String)session.getAttribute("nickname");
			
			if(nickname == null) {
				// 로그인되지 않은 경우, 오류 메시지
				result = "FAILURE";
			} else {
				// 모든 사용자에게 메시지를 추가
				String message = request.getParameter("message");
				messages.addMessageToAll("[" + nickname + "] " + message);
			
				// 성공 메시지 반환
				result = "SUCCESS";
			}
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain");
			PrintWriter writer = response.getWriter();
			writer.write(result);
			writer.flush();
			writer.close();
		}
	}
}
