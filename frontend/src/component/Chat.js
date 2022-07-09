import React, {useEffect, useState} from "react";
import {
    Avatar,
    AvatarGroup,
    Conversation, ConversationHeader,
    AddUserButton,
    ConversationList,
    MainContainer,
    Search,
    Sidebar
} from "@chatscope/chat-ui-kit-react";
import MyChatContainer from "./MyChatContainer";
import axios from "axios";
import SearchModal from "./CreateChatRoomModal";

const Chat = () => {
    const img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAATpJREFUWEdjvPm+8T/DEACMow6lciyNhiiVA5RhNERHQ5TaIUBt80bT6GiIUjsEqG0e0WlUhb+SgYmRFWz/i2/rGT79uoziFkW+PAZWJgGw2Pufxxlef9+N1a3CHA4Mwhx2YLk//z4z3PvUT5SfiHaoqkA1AyMDM9jQl982MXz8dQHFAiW+IgYWJh6w2IefZxhefd+G1QGinC4MguxWYLm//78x3P3YM+rQ0RDFlwaGdxqF+By9B8MID5BBk5kIZdNRhxIKIZA8cjkKKuy//rmNok2c04uBiZFjtBwd3rl+tArFk1mGd9Rja+Yp8RUysDDxQnP9KYZX33dgDR8RDkcGIQ5baOvpK8Pdj73EFDqj/XqiQokURUSnUVIMpYXaUYdSO1RHQ3Q0RKkdAtQ2bzSNjoYotUOA2uYBAI6umQqSmDikAAAAAElFTkSuQmCC";
    const [rooms, setRooms] = useState();
    const [selectedRoom, setSelectedRoom] = useState({roomId: -1});
    const [showFriendsModal, setShowFriendsModal] = useState(false);
    const currentUser = useState(JSON.parse(localStorage.getItem('user')));

    const getAllRooms = () => {
        axios.get("/api/v1/rooms").then(response => {
            setRooms(response.data);
        })
    }

    const getRoomUsers = (users) => {
        return users.filter(_user => _user.name !== currentUser[0].name);
    }

    useEffect(() => {
        getAllRooms();
    }, []);

    return (
        <>
            <SearchModal show={showFriendsModal}
                         onHide={() => setShowFriendsModal(false)}
            />
            <div style={{
                height: "600px",
                position: "relative"
            }}>
                <MainContainer responsive>
                    <Sidebar position="left" scrollable>
                        <Search placeholder="Search..."/>
                        <ConversationHeader>
                            <ConversationHeader.Content>
                                <AddUserButton border onClick={() => setShowFriendsModal(true)}/>
                            </ConversationHeader.Content>
                        </ConversationHeader>
                        <ConversationList>
                            {rooms && rooms.length > 0 &&
                                rooms.map(_room => {
                                    const roomUsers = getRoomUsers(_room.users);

                                    return (<Conversation name={roomUsers.map(_user => _user.name).join(', ')}
                                                          active={_room.roomId === selectedRoom.roomId}
                                                          onClick={() => setSelectedRoom(_room)}
                                                          info="Yes, i can do it for you"
                                                          unreadCnt={3}>
                                        <AvatarGroup size="sm">
                                            {roomUsers.map(_user => <Avatar src={img} name={_user.name}/>)}
                                        </AvatarGroup>
                                    </Conversation>)
                                })
                            }
                        </ConversationList>
                    </Sidebar>
                    {
                        selectedRoom.roomId !== -1 &&
                        <MyChatContainer room={selectedRoom}
                                         roomUsers={getRoomUsers(selectedRoom.users)}
                                         currentUser={currentUser[0]}
                        />
                    }
                </MainContainer>
            </div>
        </>
    )
}

export default Chat;