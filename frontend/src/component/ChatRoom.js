import * as React from 'react';
import {useEffect, useState} from 'react';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";

import AppBar from '@mui/material/AppBar';
import IconButton from '@mui/material/IconButton';
import ListItemButton from '@mui/material/ListItemButton';
import Toolbar from '@mui/material/Toolbar';
import SearchIcon from '@mui/icons-material/Search';
import SettingsIcon from '@mui/icons-material/Settings';
import PersonIcon from '@mui/icons-material/Person';
import ChatBubbleIcon from '@mui/icons-material/ChatBubble';
import {Badge} from "@mui/material";
import axios from "axios";
import dayjs from "dayjs";
import 'dayjs/locale/ko';

dayjs.locale('ko');

export default function ChatRoom() {
    const [rooms, setRooms] = useState();

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
        } else {
            return day.format("MMM DD") + "일"
        }

    }

    useEffect(() => {
        getAllRooms();
    }, []);

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
                <Box component="main" sx={{p: 3}}>
                    <Toolbar/>
                    <Typography>
                        <List sx={{width: '100%', maxWidth: 360, bgcolor: 'background.paper'}}>
                            {rooms && rooms.length > 0 &&
                                rooms.map(_room => {
                                    return (
                                        <>
                                            <ListItemButton
                                                alignItems="flex-start"
                                            >
                                                <ListItemAvatar>
                                                    <Avatar alt="Remy Sharp" src="/static/images/avatar/1.jpg"/>
                                                </ListItemAvatar>
                                                <ListItemText
                                                    primary={_room.users.map(_user => _user.name).join(', ')}
                                                    secondary={
                                                        <React.Fragment>
                                                            <Typography
                                                                sx={{display: 'inline'}}
                                                                component="span"
                                                                variant="body2"
                                                                color="text.primary"
                                                            >
                                                            </Typography>
                                                            {_room.lastMessage}
                                                        </React.Fragment>
                                                    }
                                                />
                                                <Typography variant="body2" color="text.secondary">
                                                    {formatDate(_room.lastMessageTime)}
                                                    <Typography variant="body2" color="text.secondary">
                                                        <Badge badgeContent={_room.unreadMessagesCount} color="error"/>
                                                    </Typography>
                                                </Typography>
                                            </ListItemButton>
                                            <Divider variant="inset" component="li" sx={{width: 300}}/>
                                        </>
                                    )
                                })
                            }
                        </List>
                    </Typography>
                </Box>
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
