import React, {useState} from "react";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import Switch from '@mui/material/Switch';
import {yellow} from "@mui/material/colors";
import { alpha, styled } from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';

const BootstrapDialog = styled(Dialog)(({theme}) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

const YellowSwitch = styled(Switch)(({ theme }) => ({
    '& .MuiSwitch-switchBase.Mui-checked': {
        color: yellow[600],
        '&:hover': {
            backgroundColor: alpha(yellow[600], theme.palette.action.hoverOpacity),
        },
    },
    '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
        backgroundColor: yellow[600],
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

const SwitchListSecondary = () => {
    const [checked, setChecked] = React.useState(['allow-search']);

    const handleToggle = (value) => () => {
        const currentIndex = checked.indexOf(value);
        const newChecked = [...checked];

        if (currentIndex === -1) {
            newChecked.push(value);
        } else {
            newChecked.splice(currentIndex, 1);
        }

        setChecked(newChecked);
    };

    return (
        <List
            sx={{ width: '100%', bgcolor: 'background.paper' }}
        >
            <ListItem sx={{px: 0}}>
                <ListItemText id="switch-list-label-only-default-profile"
                              primary="기본프로필로만 참여 허용"
                              secondary="상대방이 카카오톡 기본프로필로만 대화할 수 있게 됩니다. 생성 후에는 설정을 변경할 수 없습니다."/>
                <YellowSwitch
                    edge="end"
                    onChange={handleToggle('only-default-profile')}
                    checked={checked.indexOf('only-default-profile') !== -1}
                    inputProps={{
                        'aria-labelledby': 'switch-list-label-only-default-profile',
                    }}
                />
            </ListItem>
            <ListItem sx={{px: 0}}>
                <ListItemText id="switch-list-label-allow-search"
                              primary="검색 허용"
                              secondary="채팅방 이름과 소개를 검색할 수 있게 합니다."
                />
                <YellowSwitch
                    edge="end"
                    onChange={handleToggle('allow-search')}
                    checked={checked.indexOf('allow-search') !== -1}
                    inputProps={{
                        'aria-labelledby': 'switch-list-label-allow-search',
                    }}
                />
            </ListItem>
            <ListItem sx={{px: 0}}>
                <ListItemText id="switch-list-label-entry-conditions"
                              primary="채팅방 입장 조건 설정"
                              secondary="성별/출생연도를 입장조건으로 설정하면, 설정된 인증정보가 있는 멤버만 채팅방에 입장할 수 있습니다."
                />
                <YellowSwitch
                    edge="end"
                    onChange={handleToggle('entry-conditions')}
                    checked={checked.indexOf('entry-conditions') !== -1}
                    inputProps={{
                        'aria-labelledby': 'switch-list-label-entry-conditions',
                    }}
                />
            </ListItem>
        </List>
    );
}

const OpenGroupChatDialog = ({open, handleClose}) => {
    const [openGroupChatRoomName, setOpenGroupChatRoomName] = useState('');

    const handleChange = (event) => {
        const value = event.target.value;

        if (value.length > 30) return;

        setOpenGroupChatRoomName(value);
    };

    const createOpenGroupChatRoom = () => {
        axios.post("/api/open-chat", {
            roomName: openGroupChatRoomName
        }).then(r => {
            handleClose();
            window.location.reload(false);
        }).catch(e => {

        })
    }

    return (
        <BootstrapDialog
            onClose={handleClose}
            aria-labelledby="customized-dialog-title"
            open={open}
        >
            <BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
                그룹채팅방 만들기
            </BootstrapDialogTitle>
            <DialogContent sx={{width: 440}}>
                <TextField
                    id="standard-start-adornment"
                    sx={{width: '100%', py: 3}}
                    multiline
                    placeholder={"오픈채팅방 이름을 입력해주세요."}
                    InputProps={{
                        endAdornment: <InputAdornment position="end">{openGroupChatRoomName.length}/30</InputAdornment>,
                    }}
                    value={openGroupChatRoomName}
                    onChange={handleChange}
                    variant="standard"
                />
                <SwitchListSecondary />
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={createOpenGroupChatRoom} disabled={!openGroupChatRoomName}>
                    확인
                </Button>
            </DialogActions>
        </BootstrapDialog>
    );
}

export default OpenGroupChatDialog;