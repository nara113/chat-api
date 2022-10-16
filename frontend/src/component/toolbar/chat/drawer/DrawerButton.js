import React, {useState} from "react";
import MyProfileDialog from "../../friends/MyProfileDialog";
import ProfileDialog from "../../friends/ProfileDialog";
import InvitationDialog from "./InvitationDialog";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import ChatRoomDrawer from "./ChatRoomDrawer";

export default function DrawerButton({currentUser, currentRoom, roomUsers}) {
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
                            currentUser={currentUser[0]}
                            roomUsers={roomUsers}
                            handleMyProfileDialogOpen={handleMyProfileDialogOpen}
                            handleProfileDialogOpen={handleProfileDialogOpen}
                            handleInvitationDialogOpen={handleInvitationDialogOpen}
            />
        </>
    )
}