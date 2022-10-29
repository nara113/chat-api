package chat.api;

import chat.api.friend.entity.ChatFriend;
import chat.api.friend.repository.ChatFriendRepository;
import chat.api.message.dto.ChatType;
import chat.api.room.entity.ChatGroup;
import chat.api.room.repository.ChatGroupRepository;
import chat.api.room.entity.ChatMessage;
import chat.api.room.repository.ChatMessageRepository;
import chat.api.room.repository.MessageRepository;
import chat.api.room.entity.ChatRoom;
import chat.api.room.repository.ChatRoomRepository;
import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

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
    MessageRepository messageRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Transactional
    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            User user1 = User.createUser("email1",
                    "Lilly",
                    passwordEncoder.encode("pass1"),
                    "hi");

            User user2 = User.createUser("email2",
                    "Joe",
                    passwordEncoder.encode("pass2"));

            User user3 = User.createUser("email3",
                    "Emily",
                    passwordEncoder.encode("pass2"),
                    "hahaha");

            User user4 = User.createUser("email4",
                    "Kai",
                    passwordEncoder.encode("pass2"),
                    "message");

            User user5 = User.createUser("email5",
                    "Eliot",
                    passwordEncoder.encode("pass2"));

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

            ChatRoom room1 = ChatRoom.createChatRoom("room 1");
            ChatRoom room2 = ChatRoom.createChatRoom("room 2");
            ChatRoom room3 = ChatRoom.createChatRoom("room 3");
            ChatRoom room4 = ChatRoom.createChatRoom("room 4");

            chatRoomRepository.save(room1);
            chatRoomRepository.save(room2);
            chatRoomRepository.save(room3);
            chatRoomRepository.save(room4);

            IntStream.rangeClosed(0, 300)
                    .forEach(i -> {
                        ChatMessage chatMessage = ChatMessage.createMessage(
                                "message " + i,
                                user1,
                                room1,
                                ChatType.TALK
                        );

                        chatMessageRepository.save(chatMessage);
                    });

            ChatMessage chatMessage1 = ChatMessage.createMessage(
                    "message",
                    user1,
                    room2,
                    ChatType.TALK
            );

            ChatMessage chatMessage2 = ChatMessage.createMessage(
                    "message",
                    user1,
                    room3,
                    ChatType.TALK
            );

            ChatMessage chatMessage3 = ChatMessage.createMessage(
                    "message",
                    user1,
                    room4,
                    ChatType.TALK
            );

            chatMessageRepository.save(chatMessage1);
            chatMessageRepository.save(chatMessage2);
            chatMessageRepository.save(chatMessage3);

//            IntStream.rangeClosed(0, 300)
//                    .forEach(i -> {
//                        Message chatMessage = Message.createMessage(
//                                "message " + i,
//                                user1.getId(),
//                                room1.getId(),
//                                ChatType.TALK
//                        );
//
//                        messageRepository.save(chatMessage);
//                    });

            ChatGroup chatGroup = ChatGroup.createChatGroup(user1, room1);
            ChatGroup chatGroup2 = ChatGroup.createChatGroup(user2, room1);
            ChatGroup chatGroup3 = ChatGroup.createChatGroup(user3, room1);
            ChatGroup chatGroup4 = ChatGroup.createChatGroup(user1, room2);
            ChatGroup chatGroup5 = ChatGroup.createChatGroup(user4, room2);
            ChatGroup chatGroup6 = ChatGroup.createChatGroup(user1, room3);
            ChatGroup chatGroup7 = ChatGroup.createChatGroup(user5, room3);
            ChatGroup chatGroup8 = ChatGroup.createChatGroup(user1, room4);
            ChatGroup chatGroup9 = ChatGroup.createChatGroup(user5, room4);
            ChatGroup chatGroup10 = ChatGroup.createChatGroup(user2, room4);

            chatGroupRepository.save(chatGroup);
            chatGroupRepository.save(chatGroup2);
            chatGroupRepository.save(chatGroup3);
            chatGroupRepository.save(chatGroup4);
            chatGroupRepository.save(chatGroup5);
            chatGroupRepository.save(chatGroup6);
            chatGroupRepository.save(chatGroup7);
            chatGroupRepository.save(chatGroup8);
            chatGroupRepository.save(chatGroup9);
            chatGroupRepository.save(chatGroup10);
        };
    }

}
