import React, {useEffect, useState} from "react";
import axios from "axios";
import IconButton from "@mui/material/IconButton";
import AddCommentOutlinedIcon from "@mui/icons-material/AddCommentOutlined";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ChatBubbleOutlineOutlinedIcon from "@mui/icons-material/ChatBubbleOutlineOutlined";
import ListItemText from "@mui/material/ListItemText";
import RegularChatDialog from "./RegularChatDialog";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import ForumOutlinedIcon from "@mui/icons-material/ForumOutlined";

const CreateRoomButton = ({currentUserId}) => {
    const [friends, setFriends] = useState()
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const open = Boolean(anchorEl);

    const [modalOpen, setModalOpen] = React.useState(false);
    const handleModalOpen = () => setModalOpen(true);
    const handleModalClose = (event, reason) => {
        if (reason && reason === "backdropClick") return;

        setModalOpen(false);
    }

    const getFriends = () => {
        axios.get(`/api/v1/friends`)
            .then(res => {
                setFriends(res.data.data);
            })
    }

    useEffect(() => {
        getFriends();
    }, []);

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
                <MenuItem onClick={handleModalOpen}>
                    <ListItemIcon>
                        <ChatBubbleOutlineOutlinedIcon fontSize="small"/>
                    </ListItemIcon>
                    <ListItemText>일반채팅</ListItemText>
                </MenuItem>
                {modalOpen && <RegularChatDialog
                    open={modalOpen}
                    handleClose={handleModalClose}
                    friends={friends}
                    currentUserId={currentUserId}
                />}
                {/*<RegularChatModal*/}
                {/*    modalOpen={modalOpen}*/}
                {/*    handleModalClose={handleModalClose}*/}
                {/*    friends={friends}/>*/}
                <MenuItem onClick={handleClose}>
                    <ListItemIcon>
                        <LockOutlinedIcon fontSize="small"/>
                    </ListItemIcon>
                    <ListItemText>비밀채팅</ListItemText>
                </MenuItem>
                <MenuItem onClick={handleClose}>
                    <ListItemIcon>
                        <ForumOutlinedIcon fontSize="small"/>
                    </ListItemIcon>
                    <ListItemText>오픈채팅</ListItemText>
                </MenuItem>
            </Menu>
        </>
    )
}

export default CreateRoomButton;