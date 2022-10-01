import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import dayjs from "dayjs";
import * as StompJs from "@stomp/stompjs";
import * as SockJS from "sockjs-client";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import SearchIcon from "@mui/icons-material/Search";
import SettingsIcon from "@mui/icons-material/Settings";
import {Badge} from "@mui/material";
import PersonIcon from "@mui/icons-material/Person";
import ChatBubbleIcon from "@mui/icons-material/ChatBubble";
import ChatRoom2 from "./toolbar/chat/ChatRoom2";
import ChatFriends from "./toolbar/friends/ChatFriends";
import CreateRoomButton from "./createRoom/CreateRoomButton";

export default function AppContainer() {
    const [rooms, setRooms] = useState();
    const [selectedRoom, setSelectedRoom] = useState();
    const [newMessage, setNewMessage] = useState();
    const [toolbar, setToolbar] = useState('ROOMS');
    const currentUser = useState(JSON.parse(localStorage.getItem('user')));
    const isRoomsFirstRender = useRef(true);
    const stateRef = useRef();

    stateRef.current = selectedRoom;

    const getAllRooms = () => {
        axios.get("/api/v1/rooms").then(res => {
            setRooms(res.data.data);
        })
    }

    useEffect(() => {
        connect();

        console.log("currentUser", currentUser)

        return () => disconnect();
    }, []);

    useEffect(() => {
        if (!rooms || !isRoomsFirstRender.current) {
            return;
        }

        isRoomsFirstRender.current = false;

        subscribe();
    }, [rooms])

    const client = useRef({});

    const connect = () => {
        client.current = new StompJs.Client({
            webSocketFactory: () => new SockJS("/ws/chat"),
            connectHeaders: {
                "auth-token": "spring-chat-auth-token",
            },
            debug: function (str) {
                console.log(str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                getAllRooms();
            },
            onStompError: (frame) => {
                console.error(frame);
            },
        });

        client.current.activate();
    };

    const disconnect = () => {
        client.current.deactivate();
    };

    const subscribe = () => {
        const userId = currentUser[0].userId;

        client.current.subscribe(`/queue/user/${userId}`, ({body}) => {
            const message = JSON.parse(body);

            const selectedRoom = stateRef.current;

            if (selectedRoom && message.roomId === selectedRoom.roomId) {
                setNewMessage(message);

                client.current.publish({
                    destination: "/app/chat/message/read",
                    body: JSON.stringify({roomId: message.roomId, userId: userId, messageId: message.messageId}),
                });
            }

            setRooms(prevState => {
                return prevState.map(room => {
                    if (room.roomId === message.roomId) {
                        return {
                            ...room,
                            unreadMessagesCount: (selectedRoom && selectedRoom.roomId === message.roomId)
                                ? room.unreadMessagesCount
                                : room.unreadMessagesCount + 1,
                            lastMessage: message.message,
                            lastMessageTime: message.createdDate
                        }
                    }

                    return room;
                }).sort((a, b) => {
                    if (dayjs(a.lastMessageTime).isAfter(b.lastMessageTime)) {
                        return -1;
                    }

                    return 1;
                });
            });
        });
    };

    return (
        <Container sx={{
            display: 'flex',
            justifyContent: 'center',
        }}>
            <Box sx={{display: 'flex'}}>
                <AppBar component="nav">
                    <Toolbar>
                        <Typography
                            variant="h6"
                            component="div"
                            sx={{flexGrow: 1, display: {xs: 'none', sm: 'block'}}}
                        >
                            {toolbar === 'FRIENDS' ? "친구" : "채팅"}
                        </Typography>
                        <Box sx={{display: {xs: 'none', sm: 'block'}}}>
                            <IconButton color="inherit">
                                <SearchIcon/>
                            </IconButton>
                            <CreateRoomButton currentUserId={currentUser[0].userId}/>
                            <IconButton color="inherit">
                                <SettingsIcon/>
                            </IconButton>
                        </Box>
                    </Toolbar>
                </AppBar>
                {
                    toolbar === 'FRIENDS'
                        ? <ChatFriends currentUser={currentUser[0]}/>
                        : <ChatRoom2 client={client}
                                     currentUser={currentUser}
                                     newMessage={newMessage}
                                     rooms={rooms}
                                     setRooms={setRooms}
                                     selectedRoom={selectedRoom}
                                     setSelectedRoom={setSelectedRoom}/>
                }
            </Box>
            <AppBar position="fixed" color="primary" sx={{top: 'auto', bottom: 0}}>
                <Toolbar>
                    <Box sx={{flexGrow: 1}}/>
                    <IconButton
                        onClick={() => setToolbar('FRIENDS')}
                        color="inherit">
                        <PersonIcon/>
                    </IconButton>
                    <IconButton
                        onClick={() => setToolbar('ROOMS')}
                        color="inherit">
                        <Badge badgeContent={
                            rooms
                            && rooms.length > 0
                            && rooms.map(room => room.unreadMessagesCount)
                                .reduce((accumulator, curr) => accumulator + curr)}
                               color="error">
                            <ChatBubbleIcon/>
                        </Badge>
                    </IconButton>
                </Toolbar>
            </AppBar>
        </Container>
    );
}