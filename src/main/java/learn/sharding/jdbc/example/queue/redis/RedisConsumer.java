package learn.sharding.jdbc.example.queue.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import learn.sharding.jdbc.example.websocket.model.MessageModel;
import learn.sharding.jdbc.example.websocket.server.SocketServer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sunyong on 2018-11-13.
 */
@Service
public class RedisConsumer {

    private String persons = "sunyong,123";

    public void receiveMessage(String message) {
        List<MessageModel> messageModels = JSONArray.parseArray(message, MessageModel.class);
        Set<String> tempSet = new HashSet<>(Arrays.asList(persons.split(",")));
        Set<String> stringSet = tempSet.stream().collect(Collectors.toSet());
        Iterator<String> iterator = stringSet.iterator();
        while (iterator.hasNext()) {
            String userId = iterator.next();
            messageModels.stream()
                    .filter(t -> t.getUserId().equals(userId))
                    .findAny()
                    .ifPresent(t -> {
                        SocketServer.sendMessage(t.getContent(), t.getUserId());
                    });
            System.out.println(message);
        }
    }
}
