import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import React, {useEffect, useState} from "react";
import axios from "axios";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Divider from '@mui/material/Divider';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import MyProfileDialog from "./MyProfileDialog";
import ProfileDialog from "./ProfileDialog";
import ListItemButton from "@mui/material/ListItemButton";

export default function ChatFriends() {
    const [friends, setFriends] = useState();
    const [myProfileDialogOpen, setMyProfileDialogOpen] = useState(false);
    const [profileDialogOpen, setProfileDialogOpen] = useState(false);
    const [selectedFriend, setSelectedFriend] = useState();
    const [currentUser, setCurrentUser] = useState({
        name: '',
        userId: '',
        profileUrl: '',
        statusMessage: ''
    });

    const handleMyProfileDialogOpen = () => setMyProfileDialogOpen(true);
    const handleMyProfileDialogClose = () => setMyProfileDialogOpen(false);

    const handleProfileDialogOpen = (friend) => {
        setProfileDialogOpen(true);
        setSelectedFriend(friend)
    }
    const handleProfileDialogClose = () => setProfileDialogOpen(false);

    const getFriends = () => {
        axios.get(`/api/friends`)
            .then(res => {
                setFriends(res.data.data);
            })
    }

    const getCurrentUser = () => {
        axios.get(`/api/users/current`)
            .then(res => {
                setCurrentUser(res.data.data);
            })
    }

    useEffect(() => {
        getFriends();
        getCurrentUser();
    }, []);

    return (
        <Box sx={{flexGrow: 1, overflow: 'hidden', px: 3}}>
            {/*<Toolbar/>*/}
            {myProfileDialogOpen && <MyProfileDialog
                open={myProfileDialogOpen}
                handleClose={handleMyProfileDialogClose}
                currentUser={currentUser}
            />}
            <List
                sx={{
                    width: '100%',
                    maxWidth: 360,
                    bgcolor: 'background.paper',
                }}
            >
                <ListItem alignItems="flex-start"
                          sx={{pl: 0}}>
                    <ListItemButton
                        sx={{p: 0}}
                        onClick={handleMyProfileDialogOpen}>
                        <ListItemAvatar>
                            <Avatar sx={{borderRadius: 4}} key={currentUser.userId} src={currentUser.profileUrl}/>
                        </ListItemAvatar>
                        <ListItemText
                            primary={currentUser.name}
                            secondary={
                                <React.Fragment>
                                    <Typography
                                        sx={{display: 'inline'}}
                                        component="span"
                                        variant="body2"
                                        color="text.primary"
                                    >
                                    </Typography>
                                    {currentUser.statusMessage}
                                </React.Fragment>
                            }
                            sx={{width: 500}}
                        />
                    </ListItemButton>
                </ListItem>
                <Divider component="li"/>
                <li>
                    <Typography
                        sx={{mt: 1}}
                        color="text.secondary"
                        display="block"
                        variant="caption"
                    >
                        친구 {friends && friends.length}
                    </Typography>
                </li>
                {profileDialogOpen && <ProfileDialog
                    open={profileDialogOpen}
                    handleClose={handleProfileDialogClose}
                    user={selectedFriend}
                />}
                {
                    friends && friends.length > 0 &&
                    friends.map((_friend) => {
                        return (
                            <ListItem alignItems="flex-start"
                                      sx={{pl: 0}}>
                                <ListItemButton
                                    sx={{p: 0}}
                                    onClick={() => handleProfileDialogOpen(_friend)}>
                                    <ListItemAvatar>
                                        <Avatar sx={{borderRadius: 4}} key={_friend.userId} src={_friend.profileUrl}/>
                                    </ListItemAvatar>
                                    <ListItemText
                                        primary={_friend.name}
                                        secondary={
                                            <React.Fragment>
                                                <Typography
                                                    sx={{display: 'inline'}}
                                                    component="span"
                                                    variant="body2"
                                                    color="text.primary"
                                                >
                                                </Typography>
                                                {_friend.statusMessage}
                                            </React.Fragment>
                                        }
                                        sx={{width: 500}}
                                    />
                                </ListItemButton>
                            </ListItem>
                        )
                    })}
            </List>
        </Box>
    )
}