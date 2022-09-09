import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import Box from "@mui/material/Box";
import Grid from '@mui/material/Grid';
import {styled} from '@mui/material/styles';
import Paper from '@mui/material/Paper';
import Toolbar from '@mui/material/Toolbar';
import {Badge} from "@mui/material";
import dayjs from "dayjs";
import 'dayjs/locale/ko';

import React from "react";
import ChatContainer2 from "./ChatContainer2";

dayjs.locale('ko');

const StyledPaper = styled(Paper)(({theme}) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(2),
    maxWidth: 400,
    color: theme.palette.text.primary,
}));

export default function ChatRoom2({
                                     client,
                                     currentUser,
                                     newMessage,
                                     rooms,
                                     setRooms,
                                     selectedRoom,
                                     setSelectedRoom
}) {
    const onClickRoom = (room) => {
        setSelectedRoom(room);
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

    return (
        <>
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
                        currentRoom={selectedRoom}
                        roomUsers={selectedRoom.users}
                        currentUser={currentUser[0]}
                        newMessage={newMessage}
                        setRooms={setRooms}
                    />
                </Box>
            }
        </>
    );
}