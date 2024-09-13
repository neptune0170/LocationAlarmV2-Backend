package com.locationalarm.Events.dtos;

public class CoordinateMessage {
    private String userId;
    private String coordinates;
    private String groupId;

    public String getUserId() {
        return userId;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "CoordinateMessage{" +
                "userId='" + userId + '\'' +
                ", coordinates='" + coordinates + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
