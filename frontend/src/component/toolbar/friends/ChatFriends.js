import Toolbar from "@mui/material/Toolbar";
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

export default function ChatFriends({currentUser}) {
    const [friends, setFriends] = useState();
    const [myProfileDialogOpen, setMyProfileDialogOpen] = useState(false);

    const handleProfileModalOpen = () => setMyProfileDialogOpen(true);
    const handleProfileModalClose = () => setMyProfileDialogOpen(false);

    const getFriends = () => {
        axios.get(`/api/v1/friends`)
            .then(res => {
                setFriends(res.data.data);
            })
    }

    useEffect(() => {
        getFriends();
        console.log(currentUser)
    }, []);

    return (
        <Box sx={{flexGrow: 1, overflow: 'hidden', px: 3}}>
            <Toolbar/>
            {myProfileDialogOpen && <MyProfileDialog
                open={myProfileDialogOpen}
                setOpen={setMyProfileDialogOpen}
                handleClose={handleProfileModalClose}
            />}
            <List
                sx={{
                    width: '100%',
                    maxWidth: 360,
                    bgcolor: 'background.paper',
                }}
            >
                <ListItem alignItems="flex-start"
                          onDoubleClick={handleProfileModalOpen}
                          sx={{pl: 0}}>
                    <ListItemAvatar>
                        <Avatar key={currentUser.userId} src={currentUser.profileUrl}/>
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
                {
                    friends && friends.length > 0 &&
                    friends.map((_friend) => {
                        return (
                            // <List sx={{width: '800', maxWidth: 800, bgcolor: 'background.paper'}}>
                            <ListItem alignItems="flex-start" sx={{pl: 0}}>
                                <ListItemAvatar>
                                    <Avatar key={_friend.userId} src={_friend.profileUrl}/>
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
                                                {/*{_friend.statusMessage}*/}
                                            </Typography>
                                            {_friend.statusMessage}
                                        </React.Fragment>
                                    }
                                    sx={{width: 500}}
                                />
                            </ListItem>
                            // </List>
                        )
                    })}
            </List>
            {/*{friends && friends.length > 0 &&*/}
            {/*    friends.map((_friend) => {*/}
            {/*        return (*/}
            {/*            <StyledPaper*/}
            {/*                sx={{*/}
            {/*                    my: 1,*/}
            {/*                    mx: 'auto',*/}
            {/*                    p: 2,*/}
            {/*                    width: 800*/}
            {/*                }}*/}
            {/*            >*/}
            {/*                <Grid container wrap="nowrap" spacing={2}>*/}
            {/*                    <Grid item>*/}
            {/*                        <Avatar key={_friend.userId} src={_friend.profileUrl}/>*/}
            {/*                    </Grid>*/}
            {/*                    <Grid item xs={12} sm container zeroMinWidth>*/}
            {/*                        <Grid item xs zeroMinWidth>*/}
            {/*                            <Typography*/}
            {/*                                noWrap>{_friend.name}</Typography>*/}
            {/*                            <Typography variant="body2" color="text.secondary">*/}
            {/*                                {_friend.statusMessage}*/}
            {/*                            </Typography>*/}
            {/*                        </Grid>*/}
            {/*                    </Grid>*/}
            {/*                </Grid>*/}
            {/*            </StyledPaper>*/}
            {/*        )*/}
            {/*    })*/}
            {/*}*/}
        </Box>
    )
}