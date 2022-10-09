import React, {useEffect, useState} from "react";
import {styled} from "@mui/material/styles";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import PropTypes from "prop-types";
import axios from "axios";
import ArrowBackIosIcon from "@mui/icons-material/ArrowBackIos";
import DialogContent from "@mui/material/DialogContent";
import Box from "@mui/material/Box";
import AvatarGroup from "@mui/material/AvatarGroup";
import Avatar from "@mui/material/Avatar";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import {DialogContentText} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import ChatParticipantsDialogContent from "../commons/ChatParticipantsDialogContent";

const BootstrapDialog = styled(Dialog)(({theme}) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

const BootstrapDialogTitle = (props) => {
    const {children, onClose, ...other} = props;

    return (
        <DialogTitle sx={{m: 0, p: 2}} {...other}>
            {children}
            {onClose ? (
                <IconButton
                    aria-label="close"
                    onClick={onClose}
                    sx={{
                        position: 'absolute',
                        right: 8,
                        top: 8,
                        color: (theme) => theme.palette.grey[500],
                    }}
                >
                    <CloseIcon/>
                </IconButton>
            ) : null}
        </DialogTitle>
    );
};

BootstrapDialogTitle.propTypes = {
    children: PropTypes.node,
    onClose: PropTypes.func.isRequired,
};

const GroupChatInfoDialogContent = ({handleClose, participantUsers, currentUserId, setDialogContent}) => {
    const [roomName, setRoomName] = useState('');
    const placeholder = participantUsers.slice(0, 5).map(user => user.name).join(', ');

    const handleChange = (event) => {
        const value = event.target.value;

        if (value.length > 50) return;

        setRoomName(value);
    };

    const createRoom = () => {
        const name = (roomName || placeholder);

        axios.post("/api/rooms", {
                roomName: name,
                'participantUserIds': [...participantUsers.map(user => user.userId), currentUserId]
            }).then(r => {
            handleClose();
            window.location.reload(false);
        }).catch(e => {

        })
    }

    const handlePrevious = () => {
        setDialogContent("ChatParticipants")
    }

    return (
        <>
            <BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
                <IconButton type="button"
                            onClick={handlePrevious}
                            sx={{p: 0}}>
                    <ArrowBackIosIcon/>
                </IconButton>
                그룹채팅방 정보 설정
            </BootstrapDialogTitle>
            <DialogContent sx={{width: 400}}>
                <Box
                    noValidate
                    component="form"
                    sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        m: 'auto',
                        width: 'fit-content',
                    }}
                >
                    <AvatarGroup
                        max={4}>
                        {
                            participantUsers
                                .map(user => {
                                    return <Avatar key={user.userId} src={user.profileUrl}>{user.name}</Avatar>
                                })
                        }
                    </AvatarGroup>
                </Box>
                <TextField
                    id="standard-start-adornment"
                    sx={{width: 400, py: 3}}
                    multiline
                    placeholder={placeholder}
                    InputProps={{
                        endAdornment: <InputAdornment position="end">{roomName.length}/50</InputAdornment>,
                    }}
                    value={roomName}
                    onChange={handleChange}
                    variant="standard"
                />
                <DialogContentText>
                    채팅시작 전, 내가 설정한 그룹채팅방의 사진과 이름은 다른 모든 대화상대에게도 동일하게 보입니다.
                    채팅시작 후 설정한 사진과 이름은 나에게만 보입니다.
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={createRoom}>
                    확인
                </Button>
            </DialogActions>
        </>
    );
}

const RegularChatDialog = ({open, handleClose, currentUserId}) => {
    const [participantUsers, setParticipantUsers] = useState([]);
    const [dialogContent, setDialogContent] = useState();

    const dialogContentMap = {
        "ChatParticipants": <ChatParticipantsDialogContent dialogTitle={'대화상대 선택'}
                                                           handleClose={handleClose}
                                                           participantUsers={participantUsers}
                                                           setParticipantUsers={setParticipantUsers}
                                                           handleNext={() => setDialogContent("GroupChatInfo")}
        />,
        "GroupChatInfo": <GroupChatInfoDialogContent currentUserId={currentUserId}
                                        handleClose={handleClose}
                                        participantUsers={participantUsers}
                                        setDialogContent={setDialogContent}
        />
    }

    useEffect(() => {
        setDialogContent("ChatParticipants");
    }, [])

    return (
        <BootstrapDialog
            onClose={handleClose}
            aria-labelledby="customized-dialog-title"
            open={open}
        >
            {dialogContent && dialogContentMap[dialogContent]}
        </BootstrapDialog>
    );
}

export default RegularChatDialog;