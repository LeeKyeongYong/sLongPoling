package com.longpolling.chatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messages {

    private static Messages messages;
    private Map<String, List<String>> messageMap;

    private Messages() {
        messageMap = new HashMap<>();
    }

    public static Messages getInstance() {
        if (messages == null) {
            messages = new Messages();
        }
        return messages;
    }

    public void addNickname(String nickname) {
        messageMap.put(nickname, new ArrayList<>());
        addMessageToAll("[" + nickname + "] 님이 입장하셨습니다.");
    }

    public void addMessageToAll(String message) {
        for (String key : messageMap.keySet()) {
            messageMap.get(key).add(message);
        }
    }

    public List<String> getMyMessages(String nickname) {
        return messageMap.get(nickname);
    }

    public void removeMyMessage(String nickname) {
        List<String> messageList = messageMap.get(nickname);
        if (messageList != null) {
            messageList.clear();
        }
    }

    public boolean existNickname(String nickname) {
        for (String key : messageMap.keySet()) {
            if (key.equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public void removeNickname(String nickname) {
        addMessageToAll("[" + nickname + "] 님이 퇴장하셨습니다.");
        messageMap.remove(nickname);
    }
}
