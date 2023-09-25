package com.example.note.Config;

import com.example.note.Model.Note;
import com.example.note.Model.Notification;
import com.example.note.Repository.NoteRepository;
import com.example.note.Service.NoteService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ScheduleConfig {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private NoteRepository noteRepository;
//    @Scheduled(fixedRate = 60000)
    public void schedul(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            String username = authentication.getName();
            List<Note> noteList = noteRepository.findAll();
            LocalDateTime currentTime = LocalDateTime.now();
            for (Note note: noteList){
                if(note.getEnd_at().isBefore(currentTime) && note.getEnd_at().isAfter(currentTime.minusMinutes(1))){
                    Notification notification = new Notification();
                    String content = "Ghi chu co id la"+notification.getId()+"và tiêu đề là"+note.getTitle()+" da het han ";

                    redisTemplate.opsForValue().set("notification:" + notification.getId(), content);

                    // Đặt TTL cho thông báo
                    redisTemplate.expire("notification:" + notification.getId(), 100, TimeUnit.HOURS);
                    messagingTemplate.convertAndSendToUser(
                            username,  // Tên người dùng hoặc máy khách cần gửi đến
                            "/queue/notifications",  // Địa chỉ đích trên WebSocket
                            content
                    );
                }
            }
        }



    }


}
