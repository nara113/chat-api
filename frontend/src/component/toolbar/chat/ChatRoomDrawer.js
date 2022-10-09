import Drawer from "@mui/material/Drawer";
import React from "react";
import Box from "@mui/material/Box";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import {ListSubheader} from "@mui/material";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar2 from "../../commons/Avatar2";
import AddIcon from "@mui/icons-material/Add";
import Typography from "@mui/material/Typography";

export default function ChatRoomDrawer({
                                           drawerOpen,
                                           toggleDrawer,
                                           currentUser,
                                           roomUsers,
                                           handleMyProfileDialogOpen,
                                           handleProfileDialogOpen,
                                           handleInvitationDialogOpen
                                       }) {
    const list = () => (
        <Box
            sx={{width: 250}}
            role="presentation"
            onClick={toggleDrawer(false)}
            onKeyDown={toggleDrawer(false)}
        >
            <List>
                <ListItem disablePadding>
                    <ListItemButton>
                        <ListItemText primary={'톡게시판'}/>
                    </ListItemButton>
                </ListItem>
            </List>
            <Divider/>
            <List
                sx={{
                    width: '100%',
                    maxWidth: 360,
                    bgcolor: 'background.paper',
                }}
                component="nav"
                aria-labelledby="nested-list-subheader"
                subheader={
                    <ListSubheader component="div" id="nested-list-subheader">
                        대화상대
                    </ListSubheader>
                }
            >
                <ListItem>
                    <ListItemButton
                        sx={{p: 0}}
                        onClick={handleInvitationDialogOpen}>
                        <ListItemAvatar>
                            <Avatar2 sx={{borderRadius: 4}}>
                                <AddIcon/>
                            </Avatar2>
                        </ListItemAvatar>
                        <ListItemText primary="대화상대 초대"/>
                    </ListItemButton>
                </ListItem>
                <ListItem>
                    <ListItemButton
                        onClick={handleMyProfileDialogOpen}
                        sx={{p: 0}}>
                        <ListItemAvatar>
                            <Avatar2 sx={{borderRadius: 4}} key={currentUser.userId} src={currentUser.profileUrl}/>
                        </ListItemAvatar>
                        <ListItemText
                            primary={'(나) ' + currentUser.name}
                            secondary={
                                <React.Fragment>
                                    <Typography
                                        sx={{display: 'inline'}}
                                        component="span"
                                        variant="body2"
                                        color="text.primary"
                                    >
                                    </Typography>
                                </React.Fragment>
                            }
                            sx={{width: 500}}
                        />
                    </ListItemButton>
                </ListItem>
                {
                    roomUsers && roomUsers.length > 0 &&
                    roomUsers.map((_friend) => {
                        return (
                            <ListItem>
                                <ListItemButton
                                    sx={{p: 0}}
                                    onClick={() => handleProfileDialogOpen(_friend)}>
                                    <ListItemAvatar>
                                        <Avatar2 sx={{borderRadius: 4}} key={_friend.userId} src={_friend.profileUrl}/>
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
    );

    return (
        <Drawer
            anchor={'right'}
            open={drawerOpen}
            onClose={toggleDrawer(false)}
        >
            {list()}
        </Drawer>
    )
}