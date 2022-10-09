import {
    Avatar,
    ChatContainer,
    ConversationHeader,
    MainContainer,
    Message,
    MessageInput,
    MessageList
} from "@chatscope/chat-ui-kit-react";
import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import dayjs from "dayjs";
import MenuIcon from '@mui/icons-material/Menu';
import IconButton from "@mui/material/IconButton";
import ProfileDialog from "../friends/ProfileDialog";
import InvitationDialog from "./InvitationDialog";
import ChatRoomDrawer from "./ChatRoomDrawer";
import MyProfileDialog from "../friends/MyProfileDialog";

const MyChatContainer = ({client, currentRoom, roomUsers, currentUser, newMessage, setRooms}) => {
    const img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAATpJREFUWEdjvPm+8T/DEACMow6lciyNhiiVA5RhNERHQ5TaIUBt80bT6GiIUjsEqG0e0WlUhb+SgYmRFWz/i2/rGT79uoziFkW+PAZWJgGw2Pufxxlef9+N1a3CHA4Mwhx2YLk//z4z3PvUT5SfiHaoqkA1AyMDM9jQl982MXz8dQHFAiW+IgYWJh6w2IefZxhefd+G1QGinC4MguxWYLm//78x3P3YM+rQ0RDFlwaGdxqF+By9B8MID5BBk5kIZdNRhxIKIZA8cjkKKuy//rmNok2c04uBiZFjtBwd3rl+tArFk1mGd9Rja+Yp8RUysDDxQnP9KYZX33dgDR8RDkcGIQ5baOvpK8Pdj73EFDqj/XqiQokURUSnUVIMpYXaUYdSO1RHQ3Q0RKkdAtQ2bzSNjoYotUOA2uYBAI6umQqSmDikAAAAAElFTkSuQmCC";

    const [chatMessages, setChatMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [users, setUsers] = useState();
    const [oldestMessageId, setOldestMessageId] = useState();
    const [loadingMore, setLoadingMore] = useState(false);
    const [last, setLast] = useState(false)
    const isUsersFirstRender = useRef(true);

    useEffect(() => {
        const subscription = client.current.subscribe(`/topic/room/${currentRoom.roomId}`, ({body}) => {
            const message = JSON.parse(body);
            setUsers(prevState => {
                return prevState.map(user => {
                    if (user.userId === message.userId) {
                        return {
                            ...user,
                            lastReadMessageId: message.messageId
                        }
                    }

                    return user;
                })
            })
        })

        setRooms(prevState => {
            return prevState.map(room => {
                if (room.roomId === currentRoom.roomId) {
                    return {
                        ...room,
                        unreadMessagesCount: 0
                    }
                }

                return room;
            });
        });

        axios.get(`/api/rooms/${currentRoom.roomId}/last-read`)
            .then(res => {
                setUsers(res.data.data);
            })

        client.current.publish({
            destination: "/app/chat/enter",
            body: JSON.stringify({roomId: currentRoom.roomId, senderId: currentUser.userId}),
        });

        return () => {
            subscription.unsubscribe()
            setChatMessages([])
            setMessage("")
            setOldestMessageId(null);
            setLoadingMore(false)
            setLast(false)
            isUsersFirstRender.current = true;
        }
    }, [currentRoom]);

    useEffect(() => {
        if (!users || !isUsersFirstRender.current) return;

        isUsersFirstRender.current = false;

        loadMessages();
    }, [users])

    useEffect(() => {
        if (!newMessage) {
            return;
        }

        setChatMessages((_chatMessages) => [..._chatMessages, newMessage]);
    }, [newMessage])

    const loadMessages = () => {
        setLoadingMore(true);

        axios.get(`/api/rooms/${currentRoom.roomId}/messages${oldestMessageId ? `?messageId=${oldestMessageId}` : ''}`)
            .then(res => {
                const data = res.data.data.reverse();

                setLoadingMore(false);

                if (Array.isArray(data) && data.length === 0) {
                    setLast(true);
                    return;
                }

                setOldestMessageId(data[0].messageId)
                setChatMessages(data.concat(chatMessages));
            })
    }

    const makeMessage = (_chatMessage) => {
        return _chatMessage.senderId === currentUser.userId
            ? <Message key={_chatMessage.messageId}
                       model={{
                           message: _chatMessage.message,
                           sentTime: "15 mins ago",
                           sender: _chatMessage.senderName,
                           direction: "outgoing",
                           position: "last"
                       }}>
                <Message.Footer sentTime={getUnreadCount(_chatMessage)}/>
            </Message>
            : <Message key={_chatMessage.messageId}
                       model={{
                           message: _chatMessage.message,
                           sentTime: "15 mins ago",
                           sender: _chatMessage.senderName,
                           direction: "incoming",
                           position: "first"
                       }}>
                <Message.Header sender={_chatMessage.senderName}/>
                <Avatar src={img} name={_chatMessage.senderName}/>
                <Message.Footer
                    sender={getUnreadCount(_chatMessage)}
                    sentTime={dayjs(_chatMessage.createdDate).format("A hh:mm")}/>
            </Message>
    }

    const getUnreadCount = (_chatMessage) => {
        return users
            .filter(user => user.lastReadMessageId < _chatMessage.messageId)
            .length;
    }

    const publish = (message) => {
        if (!client.current.connected) {
            return;
        }

        client.current.publish({
            destination: "/app/chat/message/users",
            body: JSON.stringify({
                roomId: currentRoom.roomId,
                senderId: currentUser.userId,
                senderName: currentUser.name,
                message
            }),
        });

        setMessage("");
    };

    const onYReachStart = () => {
        if (loadingMore === true || last === true) {
            return;
        }

        loadMessages();
    };

    const [drawerOpen, setDrawerOpen] = useState(false);

    const toggleDrawer = (open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }

        setDrawerOpen(open);
    };

    const [myProfileDialogOpen, setMyProfileDialogOpen] = useState(false);
    const [profileDialogOpen, setProfileDialogOpen] = useState(false);
    const [selectedFriend, setSelectedFriend] = useState();

    const handleMyProfileDialogOpen = () => setMyProfileDialogOpen(true);
    const handleMyProfileDialogClose = () => setMyProfileDialogOpen(false);

    const handleProfileDialogOpen = (friend) => {
        setProfileDialogOpen(true);
        setSelectedFriend(friend)
    }
    const handleProfileDialogClose = () => setProfileDialogOpen(false);

    const [invitationDialogOpen, setInvitationDialogOpen] = useState(false);
    const handleInvitationDialogOpen = () => setInvitationDialogOpen(true);
    const handleInvitationDialogClose = (event, reason) => {
        if (reason && reason === "backdropClick") return;

        setInvitationDialogOpen(false);
    }

    return (
        <>
            {myProfileDialogOpen && <MyProfileDialog
                open={myProfileDialogOpen}
                handleClose={handleMyProfileDialogClose}
                currentUser={currentUser}
            />}
            {profileDialogOpen && <ProfileDialog
                open={profileDialogOpen}
                handleClose={handleProfileDialogClose}
                user={selectedFriend}
            />}
            {invitationDialogOpen &&
                <InvitationDialog invitationDialogOpen={invitationDialogOpen}
                                  handleInvitationDialogClose={handleInvitationDialogClose}
                                  currentRoom={currentRoom}
                                  roomUsers={roomUsers}
                />
            }
            <IconButton
                color="inherit"
                onClick={toggleDrawer(true)}
            >
                <MenuIcon sx={{color: "inherit"}}/>
            </IconButton>
            <ChatRoomDrawer drawerOpen={drawerOpen}
                            toggleDrawer={toggleDrawer}
                            currentUser={currentUser}
                            roomUsers={roomUsers}
                            handleMyProfileDialogOpen={handleMyProfileDialogOpen}
                            handleProfileDialogOpen={handleProfileDialogOpen}
                            handleInvitationDialogOpen={handleInvitationDialogOpen}
            />
            <div style={{
                width: "600px",
                height: "400px",
                position: "relative"
            }}>
                <MainContainer responsive>
                    <ChatContainer>
                        <ConversationHeader>
                            <ConversationHeader.Back/>
                            <Avatar src={img} name="Zoe"/>
                            <ConversationHeader.Content userName={roomUsers.map(_user => _user.name).join(', ')}/>
                        </ConversationHeader>
                        <MessageList loadingMore={loadingMore} onYReachStart={onYReachStart}>
                            {chatMessages &&
                                chatMessages.map((_chatMessage) => makeMessage(_chatMessage))
                            }
                        </MessageList>
                        <MessageInput placeholder="Type message here"
                                      value={message}
                                      onSend={() => publish(message)}
                                      onChange={val => setMessage(val)}/>
                    </ChatContainer>
                </MainContainer>
            </div>
        </>
    )
}

export default MyChatContainer;