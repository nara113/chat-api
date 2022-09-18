import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import dayjs from "dayjs";
import * as StompJs from "@stomp/stompjs";
import * as SockJS from "sockjs-client";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import SearchIcon from "@mui/icons-material/Search";
import SettingsIcon from "@mui/icons-material/Settings";
import {Badge, DialogContentText, FormControl} from "@mui/material";
import PersonIcon from "@mui/icons-material/Person";
import ChatBubbleIcon from "@mui/icons-material/ChatBubble";
import ChatRoom2 from "./ChatRoom2";
import AddCommentOutlinedIcon from '@mui/icons-material/AddCommentOutlined';
import ListItemText from '@mui/material/ListItemText';
import ListItemIcon from '@mui/material/ListItemIcon';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import ForumOutlinedIcon from '@mui/icons-material/ForumOutlined';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Modal from '@mui/material/Modal';
import Paper from '@mui/material/Paper';
import InputBase from '@mui/material/InputBase';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import FormLabel from '@mui/material/FormLabel';
import FavoriteBorder from '@mui/icons-material/FavoriteBorder';
import Favorite from '@mui/icons-material/Favorite';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import CloseIcon from '@mui/icons-material/Close';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import PropTypes from 'prop-types';
import {styled} from '@mui/material/styles';
import CancelIcon from '@mui/icons-material/Cancel';
import ChatFriends from "./ChatFriends";
import Button from "@mui/material/Button";
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import AvatarGroup from '@mui/material/AvatarGroup';

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
                                    return <Avatar key={user.userId}>{user.name}</Avatar>
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
        setParticipantUsers(participantUsers.filter(user => user.userId !== id));
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
                                            <Avatar>{user.name}</Avatar>
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
    const handleModalClose = () => setModalOpen(false);

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

const UploadButtons = () => {
    const [image, setImage] = useState();

    const handleChange = e => {
        if (e.target.files) {
            const uploadFile = e.target.files[0]
            setImage(uploadFile);

            const requestProfile = new FormData()
            requestProfile.append('image', uploadFile)

            axios.post("/api/v1/upload/profile-image", requestProfile)
                .then((r) => {
                    console.log(r)
                })
        }
    };

    return (
        <Stack direction="row" alignItems="center" spacing={2}>
            <Button variant="contained"
                    component="label">
                Upload
                <input hidden
                    accept="image/*"
                       onChange={handleChange}
                       multiple type="file"/>
            </Button>
            <IconButton color="primary" aria-label="upload picture" component="label">
                <input hidden accept="image/*" type="file"/>
                {image && image.name}
            </IconButton>
        </Stack>
    );
}

export default function AppContainer() {
    const [rooms, setRooms] = useState();
    const [selectedRoom, setSelectedRoom] = useState();
    const [newMessage, setNewMessage] = useState();
    const [toolbar, setToolbar] = useState('ROOMS');
    const currentUser = useState(JSON.parse(localStorage.getItem('user')));
    const isRoomsFirstRender = useRef(true);
    const stateRef = useRef();

    stateRef.current = selectedRoom;

    const getAllRooms = () => {
        axios.get("/api/v1/rooms").then(res => {
            setRooms(res.data.data);
        })
    }

    useEffect(() => {
        connect();

        return () => disconnect();
    }, []);

    useEffect(() => {
        if (!rooms || !isRoomsFirstRender.current) {
            return;
        }

        isRoomsFirstRender.current = false;

        subscribe();
    }, [rooms])

    const client = useRef({});

    const connect = () => {
        client.current = new StompJs.Client({
            webSocketFactory: () => new SockJS("/ws/chat"),
            connectHeaders: {
                "auth-token": "spring-chat-auth-token",
            },
            debug: function (str) {
                console.log(str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                getAllRooms();
            },
            onStompError: (frame) => {
                console.error(frame);
            },
        });

        client.current.activate();
    };

    const disconnect = () => {
        client.current.deactivate();
    };

    const subscribe = () => {
        const userId = currentUser[0].userId;

        client.current.subscribe(`/queue/user/${userId}`, ({body}) => {
            const message = JSON.parse(body);

            const selectedRoom = stateRef.current;

            if (selectedRoom && message.roomId === selectedRoom.roomId) {
                setNewMessage(message);

                client.current.publish({
                    destination: "/app/chat/message/read",
                    body: JSON.stringify({roomId: message.roomId, userId: userId, messageId: message.messageId}),
                });
            }

            setRooms(prevState => {
                return prevState.map(room => {
                    if (room.roomId === message.roomId) {
                        return {
                            ...room,
                            unreadMessagesCount: (selectedRoom && selectedRoom.roomId === message.roomId)
                                ? room.unreadMessagesCount
                                : room.unreadMessagesCount + 1,
                            lastMessage: message.message,
                            lastMessageTime: message.createdDate
                        }
                    }

                    return room;
                }).sort((a, b) => {
                    if (dayjs(a.lastMessageTime).isAfter(b.lastMessageTime)) {
                        return -1;
                    }

                    return 1;
                });
            });
        });
    };

    return (
        <Container sx={{
            display: 'flex',
            justifyContent: 'center',
        }}>
            <UploadButtons/>
            <Box sx={{display: 'flex'}}>
                <AppBar component="nav">
                    <Toolbar>
                        <Typography
                            variant="h6"
                            component="div"
                            sx={{flexGrow: 1, display: {xs: 'none', sm: 'block'}}}
                        >
                            {toolbar === 'FRIENDS' ? "친구" : "채팅"}
                        </Typography>
                        <Box sx={{display: {xs: 'none', sm: 'block'}}}>
                            <IconButton color="inherit">
                                <SearchIcon/>
                            </IconButton>
                            <CreateRoomButton currentUserId={currentUser[0].userId}/>
                            <IconButton color="inherit">
                                <SettingsIcon/>
                            </IconButton>
                        </Box>
                    </Toolbar>
                </AppBar>
                {
                    toolbar === 'FRIENDS'
                        ? <ChatFriends/>
                        : <ChatRoom2 client={client}
                                     currentUser={currentUser}
                                     newMessage={newMessage}
                                     rooms={rooms}
                                     setRooms={setRooms}
                                     selectedRoom={selectedRoom}
                                     setSelectedRoom={setSelectedRoom}/>
                }
            </Box>
            <AppBar position="fixed" color="primary" sx={{top: 'auto', bottom: 0}}>
                <Toolbar>
                    <Box sx={{flexGrow: 1}}/>
                    <IconButton
                        onClick={() => setToolbar('FRIENDS')}
                        color="inherit">
                        <PersonIcon/>
                    </IconButton>
                    <IconButton
                        onClick={() => setToolbar('ROOMS')}
                        color="inherit">
                        <Badge badgeContent={
                            rooms
                            && rooms.length > 0
                            && rooms.map(room => room.unreadMessagesCount)
                                .reduce((accumulator, curr) => accumulator + curr)}
                               color="error">
                            <ChatBubbleIcon/>
                        </Badge>
                    </IconButton>
                </Toolbar>
            </AppBar>
        </Container>
    );
}