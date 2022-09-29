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
import {Badge, DialogContentText, FormControl} from "@mui/material";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Stack from "@mui/material/Stack";
import CancelIcon from "@mui/icons-material/Cancel";
import Typography from "@mui/material/Typography";
import Paper from "@mui/material/Paper";
import InputBase from "@mui/material/InputBase";
import SearchIcon from "@mui/icons-material/Search";
import FormLabel from "@mui/material/FormLabel";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Modal from "@mui/material/Modal";
import FavoriteBorder from "@mui/icons-material/FavoriteBorder";
import Favorite from "@mui/icons-material/Favorite";

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

const GroupChatInfo = ({handleClose, participantUsers, currentUserId, setDialogContent}) => {
    const [roomName, setRoomName] = useState('');
    const placeholder = participantUsers.slice(0, 5).map(user => user.name).join(', ');

    const handleChange = (event) => {
        const value = event.target.value;

        if (value.length > 50) return;

        setRoomName(value);
    };

    const createRoom = () => {
        const name = (roomName || placeholder);

        axios
            .post("/api/v1/rooms", {
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

const ChatParticipants = ({handleClose, friends, participantUsers, setParticipantUsers, setDialogContent}) => {
    const friendsMap = new Map(
        friends.map(f => {
            return [f.userId, f];
        }),
    );

    const addUser = (id) => {
        setParticipantUsers([friendsMap.get(Number(id)), ...participantUsers]);
    }

    const removeUser = (id) => {
        setParticipantUsers(participantUsers.filter(user => user.userId !== Number(id)));
    }

    const handleChange = (event) => {
        const {checked, id} = event.target;

        if (checked) {
            addUser(id);
        } else {
            removeUser(id);
        }
    };

    const handleNext = () => {
        setDialogContent("GroupChatInfo")
    }

    return (
        <>
            <BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
                대화상대 선택
            </BootstrapDialogTitle>
            <DialogContent>
                <Stack direction="row" spacing={1}
                       sx={{width: '100%', maxWidth: 400, overflow: "auto"}}>
                    {
                        participantUsers
                            .map(user => {
                                return (
                                    <IconButton sx={{px: 0}} onClick={() => removeUser(user.userId)}>
                                        <Badge
                                            overlap="circular"
                                            anchorOrigin={{vertical: 'top', horizontal: 'right'}}
                                            badgeContent={
                                                <CancelIcon/>
                                            }
                                        >
                                            <Avatar key={user.userId} src={user.profileUrl}>{user.name}</Avatar>
                                        </Badge>
                                    </IconButton>
                                )
                            })
                    }
                </Stack>
                <Typography id="modal-modal-description" sx={{mt: 2}}>
                    <Paper
                        component="form"
                        sx={{p: '2px 4px', display: 'flex', alignItems: 'center', width: 400}}
                    >
                        <InputBase
                            sx={{ml: 1, flex: 1}}
                            placeholder="이름(초성), 전화번호 검색"
                        />
                        <IconButton type="button" sx={{p: '10px'}} aria-label="search">
                            <SearchIcon/>
                        </IconButton>
                    </Paper>
                    <FormControl sx={{pt: 2}}>
                        <FormLabel id="demo-radio-buttons-group-label" sx={{pt: 2}}>친구</FormLabel>
                        <FormGroup>
                            {friends && friends.length > 0 &&
                                friends.map((_friend) => {
                                    return (
                                        <FormControlLabel
                                            control={<Checkbox
                                                id={_friend.userId.toString()}
                                                checked={participantUsers.map(user => user.userId)
                                                    .includes(_friend.userId)}
                                                onChange={handleChange}/>}
                                            label={_friend.name}/>
                                    )
                                })
                            }
                        </FormGroup>
                    </FormControl>
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button autoFocus disabled={participantUsers.length === 0}
                        onClick={handleNext}>
                    {participantUsers.length} 확인
                </Button>
            </DialogActions>
        </>
    )
}

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    maxHeight: 600,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
    overflow: 'scroll'
};

const RegularChatModal = ({modalOpen, handleModalClose, friends}) => {
    const handleChange = (event) => {
        console.log(event.target.value)
        console.log(event.target.checked)
    };

    return (
        <Modal
            open={modalOpen}
            onClose={handleModalClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={modalStyle}>
                <Typography id="modal-modal-title" variant="h6" component="h2">
                    대화상대 선택
                </Typography>
                <Typography id="modal-modal-description" sx={{mt: 2}}>
                    <Paper
                        component="form"
                        sx={{p: '2px 4px', display: 'flex', alignItems: 'center', width: 400}}
                    >
                        <InputBase
                            sx={{ml: 1, flex: 1}}
                            placeholder="이름(초성), 전화번호 검색"
                        />
                        <IconButton type="button" sx={{p: '10px'}} aria-label="search">
                            <SearchIcon/>
                        </IconButton>
                    </Paper>
                    <FormControl sx={{pt: 2}}>
                        <FormLabel id="demo-radio-buttons-group-label" sx={{pt: 2}}>친구</FormLabel>
                        <FormGroup>
                            {friends && friends.length > 0 &&
                                friends.map((_friend) => {
                                    return (
                                        <FormControlLabel
                                            control={<Checkbox
                                                value={_friend.userId}
                                                onChange={handleChange}
                                                icon={<FavoriteBorder/>}
                                                checkedIcon={<Favorite/>}/>}
                                            label={_friend.name}/>
                                    )
                                })
                            }
                        </FormGroup>
                    </FormControl>
                </Typography>
            </Box>
        </Modal>
    )
}

const RegularChatDialog = ({open, handleClose, friends, currentUserId}) => {
    const [participantUsers, setParticipantUsers] = useState([]);
    const [dialogContent, setDialogContent] = useState();

    const dialogContentMap = {
        "ChatParticipants": <ChatParticipants handleClose={handleClose}
                                              friends={friends}
                                              participantUsers={participantUsers}
                                              setParticipantUsers={setParticipantUsers}
                                              setDialogContent={setDialogContent}
        />,
        "GroupChatInfo": <GroupChatInfo currentUserId={currentUserId}
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