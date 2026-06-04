package com.example.project.websocket;

import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.project.jpa.FCBoardRepository;
import com.example.project.jpa.entity.FCBoard;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Controller
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
	private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
	
	private final FCBoardRepository fcBoardRepository;
	private ObjectMapper mapper = new ObjectMapper();
	private String updatedSessionId = "-1";
	private String boardData = "{}";
	
	@PostConstruct
	public void init() {
		FCBoard dataFromDB = fcBoardRepository.findTopByOrderByVerIdDesc();
		if(dataFromDB != null) {
			boardData = mapper.writeValueAsString(dataFromDB);
		}
	}
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
		System.out.println("新規接続: " + session.getId());
		
		System.out.println(boardData);
		session.sendMessage(new TextMessage(boardData));
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		updatedSessionId = session.getId();
		String receiveData = message.getPayload();
		System.out.println("受信メッセージ: "+receiveData);
		
		for(WebSocketSession s: sessions) {
			if(s.isOpen() && !updatedSessionId.equals(s.getId())) {
				s.sendMessage(new TextMessage(receiveData));
			}
		}
		
		FCBoard board = mapper.readValue(receiveData, FCBoard.class);
		System.out.println(mapper.writeValueAsString(board.getNodes()));
		fcBoardRepository.save(board);
		
		boardData = receiveData;
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
		sessions.remove(session);
		System.out.println("切断: "+session.getId());
	}
}
