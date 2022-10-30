package chat.api.openchat.service;

import chat.api.openchat.entity.OpenChatRoom;
import chat.api.openchat.repository.OpenChatRoomRepository;
import chat.api.user.entity.User;
import chat.api.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenChatServiceTest {
    private final OpenChatService openChatService;
    private final OpenChatRoomRepository openChatRoomRepository;
    private final UserRepository userRepository;

    @Autowired
    public OpenChatServiceTest(OpenChatService openChatService,
                               OpenChatRoomRepository openChatRoomRepository,
                               UserRepository userRepository) {
        this.openChatService = openChatService;
        this.openChatRoomRepository = openChatRoomRepository;
        this.userRepository = userRepository;
    }

    @DisplayName("오픈채팅방 최대 인원을 초과하지 않는다.")
    @Test
    void reserveConcurrency() throws InterruptedException {
        // given
        final int THREAD_COUNT = 3;
        final int MAX_NUMBER_OF_PARTICIPANTS = 2;
        final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        OpenChatRoom test = OpenChatRoom.createOpenChatRoom("testOpenChat", MAX_NUMBER_OF_PARTICIPANTS);
        openChatRoomRepository.save(test);

        // when
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        for (int index = 0; index < THREAD_COUNT; index++) {
            final int finalIndex = index;

            executorService.execute(() -> {
                User user = User.createUser("eamil" + finalIndex, "test", "test");
                userRepository.save(user);

                openChatService.joinOpenChatRoom(test.getId(), user);
                latch.countDown();
            });
        }

        latch.await(3, TimeUnit.SECONDS);

        // then
        OpenChatRoom openChatRoom = openChatRoomRepository.findById(test.getId()).get();
        assertThat(openChatRoom.getNumberOfParticipants()).isEqualTo(MAX_NUMBER_OF_PARTICIPANTS);
    }
}