package com.locationalarm.Events.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationalarm.Events.dtos.CoordinateMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventTrackingWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LogManager.getLogger(EventTrackingWebSocketHandler.class);

    private final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sessionToGroupMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, String>> groupCoordinates = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the incoming message
        CoordinateMessage coordinateMessage = objectMapper.readValue(message.getPayload(), CoordinateMessage.class);

        String groupId = coordinateMessage.getGroupId();
        String userId = coordinateMessage.getUserId();
        logger.info("Received coordinates from User ID: {} in Group ID: {}", userId, groupId);

        // Store the user's session with their group
        sessionToGroupMap.put(session.getId(), groupId);

        // Add the session to the group and store the coordinates
        groupSessions.computeIfAbsent(groupId, k -> new CopyOnWriteArraySet<>()).add(session);
        groupCoordinates.computeIfAbsent(groupId, k -> new ConcurrentHashMap<>()).put(userId, coordinateMessage.getCoordinates());

        // Prepare the payload to be sent to the group
        Map<String, String> coordinatesInGroup = groupCoordinates.get(groupId);
        String payload = objectMapper.writeValueAsString(coordinatesInGroup);

        // Broadcast the message to all other sessions in the group, except the sender
        for (WebSocketSession groupSession : groupSessions.get(groupId)) {
            if (groupSession.isOpen() && !groupSession.getId().equals(session.getId())) {
                logger.debug("Sending coordinates to group members, excluding sender: User ID: {} in Group ID: {}", userId, groupId);
                groupSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("WebSocket connection established: Session ID {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String groupId = sessionToGroupMap.remove(session.getId());
        logger.info("WebSocket connection closed: Session ID {}", session.getId());
        if (groupId != null) {
            CopyOnWriteArraySet<WebSocketSession> sessions = groupSessions.get(groupId);
            if (sessions != null) {
                sessions.remove(session);
                logger.info("Session removed for Group ID: {} on session close.", groupId);
            }
        }
    }
}
