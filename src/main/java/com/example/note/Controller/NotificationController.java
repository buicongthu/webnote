package com.example.note.Controller;

import com.example.note.Config.CustomUserDetails;
import com.example.note.Model.Note;
import com.example.note.Model.Notification;
import com.example.note.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class NotificationController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @GetMapping("/savenotification")
    public void savenotify(Principal principal){
        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        String username = userDetails.getUsername();


            List<Note> noteList = noteRepository.findAll();
            LocalDateTime currentTime = LocalDateTime.now();
            for (Note note: noteList){
                if(note.getEnd_at().isBefore(currentTime) && note.getEnd_at().isAfter(currentTime.minusMinutes(1))){

                    UUID uuid = UUID.randomUUID();
                    String id = uuid.toString();
                    String content = note.getTitle();

                    redisTemplate.opsForValue().set("notification:" + id, content);

                    // Đặt TTL cho thông báo
                    redisTemplate.expire("notification:" + id, 100, TimeUnit.HOURS);
                    messagingTemplate.convertAndSendToUser(
                            username,  // Tên người dùng hoặc máy khách cần gửi đến
                            "/queue/notifications",  // Địa chỉ đích trên WebSocket
                            content
                    );

                }
            }

    }
    @GetMapping("/getnotify")
    public Map<String , Object> getNotify(){
        Map<String, Object> response = new HashMap<>();
        List<String> notificationList = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("notification:*");
        if(keys != null){
            keys.forEach(key -> {
                String notification = redisTemplate.opsForValue().get(key);
                notificationList.add(notification);
            });
        }
        int totalNotifications = notificationList.size();
        response.put("count",totalNotifications);
        response.put("notifications",notificationList);
        return response;
    }
}
