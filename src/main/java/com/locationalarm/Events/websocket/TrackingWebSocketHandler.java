package com.locationalarm.Events.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


public class TrackingWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LogManager.getLogger(TrackingWebSocketHandler.class);

    private final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>>groupSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sessionToGroupMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String , Map<String,String>>groupCoordinates = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        CoordinateMessage coordinateMessage = objectMapper.readValue(message.getPayload(), CoordinateMessage.class);

        String groupId = coordinateMessage.getGroupId();
        String userId = coordinateMessage.getUserId();
        logger.info("Received coordinates from User ID: {} in Group ID: {}", userId, groupId);
        sessionToGroupMap.put(session.getId(), groupId);

        groupSessions.computeIfAbsent(groupId, k -> new CopyOnWriteArraySet<>()).add(session);

        groupCoordinates.computeIfAbsent(groupId, k -> new ConcurrentHashMap<>())
                .put(userId, coordinateMessage.getCoordinates());

        Map<String, String> coordinatesInGroup = groupCoordinates.get(groupId);
        String payload = objectMapper.writeValueAsString(coordinatesInGroup);

        for (WebSocketSession groupSession : groupSessions.get(groupId)) {
            if (groupSession.isOpen()) {
                logger.debug("Sending coordinates to User ID: {} in Group ID: {}", userId, groupId);
                groupSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String groupId = sessionToGroupMap.remove(session.getId());

        if (groupId != null) {
            CopyOnWriteArraySet<WebSocketSession> sessions = groupSessions.get(groupId);
            if (sessions != null) {
                sessions.remove(session);
                logger.info("Session closed for Group ID: {}. Removed session from group.", groupId);
            }
        }
    }
}
