import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Grid from '@mui/material/Grid';
import {styled} from '@mui/material/styles';
import Paper from '@mui/material/Paper';

import AppBar from '@mui/material/AppBar';
import IconButton from '@mui/material/IconButton';
import Toolbar from '@mui/material/Toolbar';
import SearchIcon from '@mui/icons-material/Search';
import SettingsIcon from '@mui/icons-material/Settings';
import PersonIcon from '@mui/icons-material/Person';
import ChatBubbleIcon from '@mui/icons-material/ChatBubble';
import {Badge} from "@mui/material";
import axios from "axios";
import dayjs from "dayjs";
import 'dayjs/locale/ko';

import React, {useEffect, useRef, useState} from "react";
import * as StompJs from "@stomp/stompjs";
import * as SockJS from "sockjs-client";
import ChatContainer2 from "./ChatContainer2";

dayjs.locale('ko');

const StyledPaper = styled(Paper)(({theme}) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(2),
    maxWidth: 400,
    color: theme.palette.text.primary,
}));

export default function ChatRoom() {
    const [rooms, setRooms] = useState();
    const [selectedRoom, setSelectedRoom] = useState();
    const [newMessage, setNewMessage] = useState();
    const currentUser = useState(JSON.parse(localStorage.getItem('user')));
    const isRoomsFirstRender = useRef(true);
    const stateRef = useRef();

    stateRef.current = selectedRoom;

    const getAllRooms = () => {
        axios.get("/api/v1/rooms").then(res => {
            setRooms(res.data.data);
        })
    }

    const formatDate = (day) => {
        day = dayjs(day);
        const now = dayjs();
        const yesterday = now.subtract(1, "day")

        if (day.isSame(now, "day")) {
            return day.format("A hh:mm")
        } else if (day.isSame(yesterday, "day")) {
            return "어제"
        } else if (day.isSame(now, "year")) {
            return day.format("MMM DD") + "일"
        } else {
            return day.format("YYYY. MM. DD.")
        }

    }

    const onClickRoom = (room) => {
        // navigate('/chat/container',
        //     {state: {room: room, roomUsers: room.users, currentUser: currentUser[0]}});
        setSelectedRoom(room);

        console.log("click room", room)
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
                            채팅
                        </Typography>
                        <Box sx={{display: {xs: 'none', sm: 'block'}}}>
                            <IconButton color="inherit">
                                <SearchIcon/>
                            </IconButton>
                            <IconButton color="inherit">
                                <SettingsIcon/>
                            </IconButton>
                        </Box>
                    </Toolbar>
                </AppBar>
                <Box sx={{flexGrow: 1, overflow: 'hidden', px: 3}}>
                    <Toolbar/>
                    {rooms && rooms.length > 0 &&
                        rooms.map((_room) => {
                            return (
                                <StyledPaper
                                    sx={{
                                        my: 1,
                                        mx: 'auto',
                                        p: 2,
                                        width: 800
                                    }}
                                >
                                    <Grid container wrap="nowrap" spacing={2} onClick={() => onClickRoom(_room)}>
                                        <Grid item>
                                            <Avatar>W</Avatar>
                                        </Grid>
                                        <Grid item xs={12} sm container zeroMinWidth>
                                            <Grid item xs zeroMinWidth>
                                                <Typography
                                                    noWrap>{_room.users.map(_user => _user.name).join(', ')}</Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    {_room.lastMessage}
                                                </Typography>
                                            </Grid>
                                            <Grid item>
                                                <Typography variant="body2" component="div" color="text.secondary">
                                                    {formatDate(_room.lastMessageTime)}
                                                </Typography>
                                                <Badge badgeContent={_room.unreadMessagesCount} max={999}
                                                       color="error"/>
                                            </Grid>
                                        </Grid>
                                    </Grid>
                                </StyledPaper>
                            )
                        })
                    }
                </Box>
                {selectedRoom &&
                    <Box sx={{flexGrow: 1, overflow: 'hidden', px: 3}}>
                        <Toolbar/>
                        <ChatContainer2
                            client={client}
                            room={selectedRoom}
                            roomUsers={selectedRoom.users}
                            currentUserId={currentUser[0].userId}
                            newMessage={newMessage}
                        />
                    </Box>
                }
            </Box>
            <AppBar position="fixed" color="primary" sx={{top: 'auto', bottom: 0}}>
                <Toolbar>
                    <Box sx={{flexGrow: 1}}/>
                    <IconButton color="inherit">
                        <PersonIcon/>
                    </IconButton>
                    <IconButton color="inherit">
                        <Badge badgeContent={
                            rooms
                            && rooms.length > 0
                            && rooms.map(room => room.unreadMessagesCount).reduce((accumulator, curr) => accumulator + curr)}
                               color="error">
                            <ChatBubbleIcon/>
                        </Badge>
                    </IconButton>
                </Toolbar>
            </AppBar>
        </Container>
    );
}
