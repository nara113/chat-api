import React, {useState} from "react";
import IconButton from "@mui/material/IconButton";
import AddCommentOutlinedIcon from "@mui/icons-material/AddCommentOutlined";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ChatBubbleOutlineOutlinedIcon from "@mui/icons-material/ChatBubbleOutlineOutlined";
import ListItemText from "@mui/material/ListItemText";
import RegularChatDialog from "./RegularChatDialog";
import ForumOutlinedIcon from "@mui/icons-material/ForumOutlined";
import OpenGroupChatDialog from "./OpenGroupChatDialog";

const CreateRoomButton = ({currentUserId}) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const [regularChatDialogOpen, setRegularChatDialogOpen] = useState(false);
    const handleRegularChatDialogOpen = () => setRegularChatDialogOpen(true);
    const handleRegularChatDialogClose = (event, reason) => {
        if (reason && reason === "backdropClick") return;

        setRegularChatDialogOpen(false);
    }

    const [openChatAnchorEl, setOpenChatAnchorEl] = React.useState(null);
    const openOpenChat = Boolean(openChatAnchorEl);
    const handleOpenChatClick = (event) => {
        setOpenChatAnchorEl(event.currentTarget);
    };
    const handleOpenChatClose = () => {
        setOpenChatAnchorEl(null);
    };

    const [groupChatDialogOpen, setGroupChatDialogOpen] = useState(false);
    const handleGroupChatDialogOpen = () => setGroupChatDialogOpen(true);
    const handleGroupChatDialogClose = (event, reason) => {
        if (reason && reason === "backdropClick") return;

        setGroupChatDialogOpen(false);
    }

    return (
        <>
            <IconButton
                id="basic-button"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}
                color="inherit">
                <AddCommentOutlinedIcon/>
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <MenuItem onClick={handleRegularChatDialogOpen}>
                    <ListItemIcon>
                        <ChatBubbleOutlineOutlinedIcon fontSize="small"/>
                    </ListItemIcon>
                    <ListItemText>일반채팅</ListItemText>
                </MenuItem>
                {regularChatDialogOpen && <RegularChatDialog
                    open={regularChatDialogOpen}
                    handleClose={handleRegularChatDialogClose}
                    currentUserId={currentUserId}
                />}
                {/*<RegularChatModal*/}
                {/*    regularChatDialogOpen={regularChatDialogOpen}*/}
                {/*    handleRegularChatDialogClose={handleRegularChatDialogClose}*/}
                {/*    friends={friends}/>*/}
                <MenuItem onClick={handleOpenChatClick}>
                    <ListItemIcon>
                        <ForumOutlinedIcon fontSize="small"/>
                    </ListItemIcon>
                    <ListItemText>오픈채팅</ListItemText>
                </MenuItem>
                <Menu
                    id="demo-positioned-menu"
                    aria-labelledby="demo-positioned-button"
                    anchorEl={openChatAnchorEl}
                    open={openOpenChat}
                    onClose={handleOpenChatClose}
                    anchorOrigin={{
                        vertical: 'top',
                        horizontal: 'left',
                    }}
                    transformOrigin={{
                        vertical: 'top',
                        horizontal: 'left',
                    }}
                >
                    <MenuItem onClick={handleOpenChatClose}>1:1 채팅방</MenuItem>
                    <MenuItem onClick={handleGroupChatDialogOpen}>그룹 채팅방</MenuItem>
                    {groupChatDialogOpen && <OpenGroupChatDialog
                        open={groupChatDialogOpen}
                        handleClose={handleGroupChatDialogClose}
                    />}
                    <MenuItem onClick={handleOpenChatClose}>오픈프로필</MenuItem>
                </Menu>
            </Menu>
        </>
    )
}

export default CreateRoomButton;