package chat.api.openchat.service;

import chat.api.room.entity.ChatGroup;
import chat.api.room.entity.ChatRoom;
import chat.api.room.repository.ChatGroupRepository;
import chat.api.room.repository.ChatRoomRepository;
import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenChatServiceTest {
    private final OpenChatService openChatService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final UserRepository userRepository;

    @Autowired
    public OpenChatServiceTest(OpenChatService openChatService,
                               ChatRoomRepository chatRoomRepository,
                               ChatGroupRepository chatGroupRepository,
                               UserRepository userRepository) {
        this.openChatService = openChatService;
        this.chatRoomRepository = chatRoomRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.userRepository = userRepository;
    }

    @DisplayName("오픈채팅방 최대 인원을 초과하지 않는다.")
    @Test
    void reserveConcurrency() throws InterruptedException {
        // given
        final int THREAD_COUNT = 5;
        final int MAX_NUMBER_OF_PARTICIPANTS = 2;
        final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        ChatRoom chatRoom = ChatRoom.createOpenChatRoom("testOpenChat", MAX_NUMBER_OF_PARTICIPANTS);
        chatRoomRepository.save(chatRoom);

        // when
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int index = 0; index < THREAD_COUNT; index++) {
            int finalIndex = index;

            executorService.execute(() -> {
                User user = User.createUser("test_email" + finalIndex, "test", "test");
                userRepository.save(user);

                try {
                    openChatService.joinOpenChatRoom(chatRoom.getId(), user);
                } catch (IllegalArgumentException e) {

                }
                latch.countDown();
            });
        }

        latch.await();

        // then
        ChatRoom openChatRoom = chatRoomRepository.findById(chatRoom.getId()).get();
        assertThat(openChatRoom.getNumberOfParticipants()).isEqualTo(MAX_NUMBER_OF_PARTICIPANTS);
        List<ChatGroup> groups = chatGroupRepository.findByChatRoomId(chatRoom.getId());
        assertThat(groups.size()).isEqualTo(MAX_NUMBER_OF_PARTICIPANTS);
    }
}