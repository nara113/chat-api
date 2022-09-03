package chat.api;

import chat.api.entity.*;
import chat.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@EnableJpaAuditing
@SpringBootApplication
public class ApiApplication {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Autowired
    ChatFriendRepository chatFriendRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Transactional
    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            User user1 = User.builder()
                    .email("email1")
                    .password(passwordEncoder.encode("pass1"))
                    .name("Lilly")
                    .statusMessage("i'm happy")
                    .build();

            User user2 = User.builder()
                    .email("email2")
                    .password(passwordEncoder.encode("pass2"))
                    .name("Joe")
                    .build();

            User user3 = User.builder()
                    .email("email3")
                    .password(passwordEncoder.encode("pass2"))
                    .name("Emily")
                    .statusMessage("hahaha")
                    .build();

            User user4 = User.builder()
                    .email("email4")
                    .password(passwordEncoder.encode("pass2"))
                    .name("Kai")
                    .statusMessage("message 2")
                    .build();

            User user5 = User.builder()
                    .email("email5")
                    .password(passwordEncoder.encode("pass2"))
                    .name("Eliot")
                    .build();

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);

            ChatFriend chatFriend1 = ChatFriend.builder()
                    .user(user1)
                    .friend(user2)
                    .build();

            ChatFriend chatFriend2 = ChatFriend.builder()
                    .user(user1)
                    .friend(user3)
                    .build();

            ChatFriend chatFriend3 = ChatFriend.builder()
                    .user(user1)
                    .friend(user4)
                    .build();

            ChatFriend chatFriend4 = ChatFriend.builder()
                    .user(user1)
                    .friend(user5)
                    .build();

            chatFriendRepository.save(chatFriend1);
            chatFriendRepository.save(chatFriend2);
            chatFriendRepository.save(chatFriend3);
            chatFriendRepository.save(chatFriend4);

            ChatRoom room1 = ChatRoom.builder()
                    .name("room 1")
                    .build();

            ChatRoom room2 = ChatRoom.builder()
                    .name("room 2")
                    .build();

            ChatRoom room3 = ChatRoom.builder()
                    .name("room 3")
                    .build();

            ChatRoom room4 = ChatRoom.builder()
                    .name("room 4")
                    .build();

            chatRoomRepository.save(room1);
            chatRoomRepository.save(room2);
            chatRoomRepository.save(room3);
            chatRoomRepository.save(room4);

            IntStream.rangeClosed(0, 300)
                    .forEach(i -> {
                        ChatMessage chatMessage = ChatMessage.builder()
                                .message("hello " + i)
                                .senderName(user1.getName())
                                .user(user1)
                                .chatRoom(room1)
                                .build();

                        chatMessageRepository.save(chatMessage);
                    });


            ChatGroup chatGroup = ChatGroup.builder()
                    .chatRoom(room1)
                    .user(user1)
                    .build();

            ChatGroup chatGroup2 = ChatGroup.builder()
                    .chatRoom(room1)
                    .user(user2)
                    .build();

            ChatGroup chatGroup3 = ChatGroup.builder()
                    .chatRoom(room1)
                    .user(user3)
                    .build();

            ChatGroup chatGroup4 = ChatGroup.builder()
                    .chatRoom(room2)
                    .user(user1)
                    .build();

            ChatGroup chatGroup5 = ChatGroup.builder()
                    .chatRoom(room2)
                    .user(user4)
                    .build();

            ChatGroup chatGroup6 = ChatGroup.builder()
                    .chatRoom(room3)
                    .user(user1)
                    .build();

            ChatGroup chatGroup7 = ChatGroup.builder()
                    .chatRoom(room3)
                    .user(user5)
                    .build();

            chatGroupRepository.save(chatGroup);
            chatGroupRepository.save(chatGroup2);
            chatGroupRepository.save(chatGroup3);
            chatGroupRepository.save(chatGroup4);
            chatGroupRepository.save(chatGroup5);
            chatGroupRepository.save(chatGroup6);
            chatGroupRepository.save(chatGroup7);
        };
    }

}
