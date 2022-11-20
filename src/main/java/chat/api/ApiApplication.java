package chat.api;

import chat.api.friend.entity.ChatFriend;
import chat.api.friend.repository.ChatFriendRepository;
import chat.api.room.entity.ChatMessage;
import chat.api.room.repository.ChatMessageRepository;
import chat.api.room.entity.ChatRoom;
import chat.api.room.repository.ChatRoomRepository;
import chat.api.user.entity.Gender;
import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.IntStream;

@EnableRetry
@SpringBootApplication
public class ApiApplication {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatFriendRepository chatFriendRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) {
                User user1 = User.createUser("email1",
                        "Lilly",
                        passwordEncoder.encode("pass1"),
                        Gender.FEMALE,
                        LocalDate.of(1990,7,10),
                        "hi");

                User user2 = User.createUser("email2",
                        "Joe",
                        passwordEncoder.encode("pass2"),
                        Gender.MALE,
                        LocalDate.of(1994,10,10),
                        null);

                User user3 = User.createUser("email3",
                        "Emily",
                        passwordEncoder.encode("pass2"),
                        Gender.FEMALE,
                        LocalDate.of(1995,1,10),
                        "hahaha");

                User user4 = User.createUser("email4",
                        "Kai",
                        passwordEncoder.encode("pass2"),
                        Gender.FEMALE,
                        LocalDate.of(1996,1,10),
                        "message");

                User user5 = User.createUser("email5",
                        "Eliot",
                        passwordEncoder.encode("pass2"),
                        Gender.FEMALE,
                        LocalDate.of(2000,1,10),
                        null);

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
                userRepository.save(user5);

                ChatFriend chatFriend1_1 = ChatFriend.createChatFriend(user1, user2);
                ChatFriend chatFriend1_2 = ChatFriend.createChatFriend(user1, user3);
                ChatFriend chatFriend1_3 = ChatFriend.createChatFriend(user1, user4);
                ChatFriend chatFriend1_4 = ChatFriend.createChatFriend(user1, user5);
                ChatFriend chatFriend2_1 = ChatFriend.createChatFriend(user2, user1);

                chatFriendRepository.save(chatFriend1_1);
                chatFriendRepository.save(chatFriend1_2);
                chatFriendRepository.save(chatFriend1_3);
                chatFriendRepository.save(chatFriend1_4);
                chatFriendRepository.save(chatFriend2_1);

                ChatRoom room1 = ChatRoom.createChatRoom("room1");
                ChatRoom room2 = ChatRoom.createChatRoom("room2");
                ChatRoom room3 = ChatRoom.createChatRoom("room3");
                ChatRoom room4 = ChatRoom.createChatRoom("room4");

                chatRoomRepository.save(room1);
                chatRoomRepository.save(room2);
                chatRoomRepository.save(room3);
                chatRoomRepository.save(room4);

                IntStream.rangeClosed(0, 300)
                        .forEach(i -> {
                            ChatMessage chatMessage = ChatMessage.createTalkMessage(
                                    "message " + i,
                                    user1,
                                    room1
                            );

                            chatMessageRepository.save(chatMessage);
                        });

                ChatMessage chatMessage1 = ChatMessage.createTalkMessage(
                        "message",
                        user1,
                        room2
                );

                ChatMessage chatMessage2 = ChatMessage.createTalkMessage(
                        "message",
                        user1,
                        room3
                );

                ChatMessage chatMessage3 = ChatMessage.createTalkMessage(
                        "message",
                        user1,
                        room4
                );

                chatMessageRepository.save(chatMessage1);
                chatMessageRepository.save(chatMessage2);
                chatMessageRepository.save(chatMessage3);

                room1.joinChatRoom(user1);
                room1.joinChatRoom(user2);
                room1.joinChatRoom(user3);
                room2.joinChatRoom(user1);
                room2.joinChatRoom(user4);
                room3.joinChatRoom(user1);
                room3.joinChatRoom(user5);
                room4.joinChatRoom(user1);
                room4.joinChatRoom(user2);
                room4.joinChatRoom(user5);
            }
        };
    }

}
