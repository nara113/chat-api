import React, {useEffect, useState} from "react";
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
import {alpha, styled} from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Checkbox from '@mui/material/Checkbox';
import Divider from "@mui/material/Divider";
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import Typography from "@mui/material/Typography";
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import dayjs from "dayjs";
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

const BootstrapDialog = styled(Dialog)(({theme}) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

const YellowSwitch = styled(Switch)(({theme}) => ({
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

    const isEntryConditionsChecked = () => {
        return checked.indexOf('entry-condition-settings') !== -1;
    }

    const [entryConditionsChecked, setEntryConditionsChecked] = React.useState(['gender', 'yearOfBirth']);

    const handleEntryConditionsToggle = (value) => () => {
        const currentIndex = entryConditionsChecked.indexOf(value);
        const newChecked = [...entryConditionsChecked];

        if (currentIndex === -1) {
            newChecked.push(value);
        } else {
            newChecked.splice(currentIndex, 1);
        }

        setEntryConditionsChecked(newChecked);
    };

    const [startBirthYear, setStartBirthYear] = React.useState('');
    const [endBirthYear, setEndBirthYear] = React.useState('');

    const handleStartBirthYearChange = (event) => {
        setStartBirthYear(event.target.value);
    };

    const handleEndBirthYearChange = (event) => {
        setEndBirthYear(event.target.value);
    };

    const [dateOfBirthToggle, setDateOfBirthToggle] = React.useState(false);

    const handleDateOfBirthToggle = () => {
        setDateOfBirthToggle(!dateOfBirthToggle);
    }

    const rangeClosed = (start, end) => {
        return new Array(end - start + 1).fill().map((d, i) => i + start);
    }

    const getBirthYearRange = () => {
        if (startBirthYear === endBirthYear) return `${startBirthYear}년생`;
        else if (endBirthYear === '') return `${startBirthYear}년생~`;
        else if (startBirthYear === '') return `~${endBirthYear}년생`;
        else return `${startBirthYear}~${endBirthYear}년생`
    }

    const [currentUser, setCurrentUser] = useState({
        userId: '',
        name: '',
        gender: '',
        dateOfBirth: '',
        statusMessage: '',
        profileUrl: ''
    });

    const [currentUserBirthYear, setCurrentUserBirtYear] = useState('');

    const getCurrentUser = () => {
        axios.get(`/api/users/current`)
            .then(res => {
                const currentUser = res.data.data;
                setCurrentUser(currentUser);

                const currentUserBirthYear = dayjs(currentUser.dateOfBirth).year();
                setCurrentUserBirtYear(currentUserBirthYear);
                setStartBirthYear(currentUserBirthYear);
                setEndBirthYear(currentUserBirthYear);
            })
    }

    useEffect(() => {
        getCurrentUser();
    }, []);

    return (
        <List
            sx={{width: '100%', bgcolor: 'background.paper'}}
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
                    onChange={handleToggle('entry-condition-settings')}
                    checked={checked.indexOf('entry-condition-settings') !== -1}
                    inputProps={{
                        'aria-labelledby': 'switch-list-label-entry-conditions',
                    }}
                />
            </ListItem>
            {
                isEntryConditionsChecked() &&
                <>
                    <Divider sx={{py: 1}}/>
                    <ListItem
                        secondaryAction={
                            <Typography variant="body2" color="text.secondary">
                                {currentUser.gender === 'MALE' ? '남자' : '여자'}
                            </Typography>
                        }
                        disablePadding
                    >
                        <ListItemButton role={undefined} onClick={handleEntryConditionsToggle('gender')} dense>
                            <ListItemIcon>
                                <Checkbox
                                    defaultChecked
                                    sx={{
                                        px: 0,
                                        color: yellow[800],
                                        '&.Mui-checked': {
                                            color: yellow[600],
                                        },
                                    }}
                                    edge="start"
                                    checked={entryConditionsChecked.indexOf('gender') !== -1}
                                    tabIndex={-1}
                                    disableRipple
                                    inputProps={{ 'aria-labelledby': 1 }}
                                    icon={<CheckCircleOutlineIcon />} checkedIcon={<CheckCircleIcon />}
                                />
                            </ListItemIcon>
                            <ListItemText primary='성별' />
                        </ListItemButton>
                    </ListItem>
                    <ListItem
                        secondaryAction={
                            <IconButton edge="end" aria-label="comments" size={"small"} sx={{p:0}} onClick={handleDateOfBirthToggle}>
                                {getBirthYearRange()}<ArrowForwardIosIcon fontSize={"small"} />
                            </IconButton>
                        }
                        disablePadding
                    >
                        <ListItemButton role={undefined} onClick={handleEntryConditionsToggle('yearOfBirth')} dense>
                            <ListItemIcon>
                                <Checkbox
                                    defaultChecked
                                    sx={{
                                        px: 0,
                                        color: yellow[800],
                                        '&.Mui-checked': {
                                            color: yellow[600],
                                        },
                                    }}
                                    edge="start"
                                    checked={entryConditionsChecked.indexOf('yearOfBirth') !== -1}
                                    tabIndex={-1}
                                    disableRipple
                                    inputProps={{ 'aria-labelledby': 1 }}
                                    icon={<CheckCircleOutlineIcon />} checkedIcon={<CheckCircleIcon />}
                                />
                            </ListItemIcon>
                            <ListItemText primary='출생연도'/>
                        </ListItemButton>
                    </ListItem>
                    <ListItemText secondary='입장할 수 있는 출생 연도 범위를 설정합니다.'/>
                    {
                        dateOfBirthToggle &&
                        <div>
                            <FormControl variant="standard" sx={{ m: 1, minWidth: 150 }}>
                                <Select
                                    labelId="demo-simple-select-standard-label"
                                    id="demo-simple-select-standard"
                                    value={startBirthYear}
                                    onChange={handleStartBirthYearChange}
                                    label="startBirthOfDate"
                                >
                                    {
                                        rangeClosed(1943, currentUserBirthYear)
                                            .map(year => <MenuItem value={year}>{year}</MenuItem>)
                                    }
                                    <MenuItem value="">
                                        <em>제한없음</em>
                                    </MenuItem>
                                </Select>
                            </FormControl>
                            <Typography
                                component="span"
                                variant="body2"
                                color="text.primary"
                            >
                                ~
                            </Typography>
                            <FormControl variant="standard" sx={{ m: 1, minWidth: 150 }}>
                                <Select
                                    labelId="demo-simple-select-standard-label"
                                    id="demo-simple-select-standard"
                                    value={endBirthYear}
                                    onChange={handleEndBirthYearChange}
                                    label="endBirthOfDate"
                                >
                                    <MenuItem value="">
                                        <em>제한없음</em>
                                    </MenuItem>
                                    {
                                        rangeClosed(currentUserBirthYear, dayjs().year())
                                            .map(year => <MenuItem value={year}>{year}</MenuItem>)
                                    }
                                </Select>
                            </FormControl>
                            <ListItemText secondary='내 출생연도를 포함한 범위로만 지정이 가능합니다.'/>
                        </div>
                    }
                </>
            }
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
                <SwitchListSecondary/>
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