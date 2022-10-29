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
import ChatRoom from "./bottom-toolbar/chat/ChatRoom";
import ChatFriends from "./bottom-toolbar/friends/ChatFriends";
import CreateRoomButton from "./toolbar/create-chat-room/CreateRoomButton";
import CssBaseline from '@mui/material/CssBaseline';
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import {Badge} from "@mui/material";
import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import PersonIcon from "@mui/icons-material/Person";
import ChatBubbleIcon from "@mui/icons-material/ChatBubble";
import Paper from '@mui/material/Paper';
import DrawerButton from "./bottom-toolbar/chat/drawer/DrawerButton";
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import {useNavigate} from "react-router-dom";
import OpenChatButton from "./toolbar/open-chat/OpenChatButton";

export default function AppContainer() {
    let navigate = useNavigate();
    const [rooms, setRooms] = useState();
    const [selectedRoom, setSelectedRoom] = useState();
    const [newMessage, setNewMessage] = useState();
    const [toolbar, setToolbar] = useState('ROOMS');
    const currentUser = useState(JSON.parse(localStorage.getItem('user')));
    const isRoomsFirstRender = useRef(true);
    const stateRef = useRef();

    stateRef.current = selectedRoom;

    const getAllRooms = () => {
        axios.get("/api/rooms").then(res => {
            setRooms(res.data.data);
        }).catch(error => {
            if (error.response.status === 401) {
                navigate('/');
            }
            return error;
        })
    }

    useEffect(() => {
        connect();

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
                "auth-token": localStorage.getItem('jwt'),
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
            } else if (isNewRoom(message.roomId)) {

                // todo
                axios.get(`/api/rooms/${message.roomId}`)
                    .then(res => {
                        const newRoom = res.data.data;
                        setRooms(current => [newRoom, ...current]);
                    });

                return;
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
                            lastMessageTime: message.timestamp
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
        }, {"auth-token": localStorage.getItem('jwt')});
    };

    const isNewRoom = (roomId) => {
        return !rooms.map(room => room.roomId).includes(roomId);
    }

    const getTotalUnreadCount = () => {
        return rooms
            && rooms.length > 0
            && rooms.map(room => room.unreadMessagesCount)
                .reduce((accumulator, curr) => accumulator + curr)
    }

    return (
        <Container sx={{
            display: 'flex',
            justifyContent: 'center',
        }}>
            <Box sx={{pb: 7}}>
                <CssBaseline/>
                <AppBar component="nav" position="static">
                    {
                        selectedRoom
                            ? <Toolbar>
                                <IconButton
                                    color="inherit"
                                    onClick={() => setSelectedRoom(null)}
                                >
                                    <ArrowBackIosIcon sx={{color: "inherit"}}/>{getTotalUnreadCount()}
                                </IconButton>
                                <Box sx={{flexGrow: 1}}/>
                                <Box sx={{display: {xs: 'none', sm: 'block'}}}>
                                    <DrawerButton currentUser={currentUser}
                                                  currentRoom={selectedRoom}
                                                  roomUsers={selectedRoom.users}
                                    />
                                </Box>
                            </Toolbar>
                            : <Toolbar>
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
                                    <OpenChatButton />
                                    <IconButton color="inherit">
                                        <SettingsIcon/>
                                    </IconButton>
                                </Box>
                            </Toolbar>
                    }
                </AppBar>
                {
                    toolbar === 'FRIENDS'
                        ? <ChatFriends/>
                        : <ChatRoom client={client}
                                    currentUser={currentUser}
                                    newMessage={newMessage}
                                    rooms={rooms}
                                    setRooms={setRooms}
                                    selectedRoom={selectedRoom}
                                    setSelectedRoom={setSelectedRoom}/>
                }
                <Paper sx={{position: 'static', bottom: 0, left: 0, right: 0}} elevation={3} position="static">
                    <BottomNavigation
                        showLabels
                        value={toolbar}
                        onChange={(event, newValue) => {
                            setToolbar(newValue);
                        }}
                    >
                        <BottomNavigationAction value="FRIENDS" icon={<PersonIcon/>}/>
                        <BottomNavigationAction value="ROOMS" icon={
                            <Badge badgeContent={getTotalUnreadCount()}
                                   color="error">
                                <ChatBubbleIcon/>
                            </Badge>
                        }/>
                        <BottomNavigationAction value="MORE" icon={<MoreHorizIcon/>}/>
                    </BottomNavigation>
                </Paper>
            </Box>
        </Container>
    );
}