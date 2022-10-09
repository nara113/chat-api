import DialogContent from "@mui/material/DialogContent";
import Stack from "@mui/material/Stack";
import IconButton from "@mui/material/IconButton";
import {Badge, FormControl} from "@mui/material";
import CancelIcon from "@mui/icons-material/Cancel";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import Paper from "@mui/material/Paper";
import InputBase from "@mui/material/InputBase";
import SearchIcon from "@mui/icons-material/Search";
import FormLabel from "@mui/material/FormLabel";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import React, {useEffect, useRef, useState} from "react";
import DialogTitle from "@mui/material/DialogTitle";
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";

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

export default function ChatParticipantsDialogContent({
                                             dialogTitle,
                                             handleClose,
                                             participantUsers,
                                             setParticipantUsers,
                                             handleNext,
                                                          participatingUserIds = []
                                         }) {
    const [friends, setFriends] = useState()
    const [filteredFriends, setFilteredFriends] = useState()
    const [friendsMap, setFriendsMap] = useState()
    const [searchText, setSearchText] = useState('')
    const isFriendsFirstRender = useRef(true);

    const getFriends = () => {
        axios.get(`/api/friends`)
            .then(res => {
                setFriends(res.data.data);
                setFilteredFriends(res.data.data);
            })
    }

    useEffect(() => {
        getFriends();
    }, []);

    useEffect(() => {
        if (!friends || !isFriendsFirstRender.current) return;

        isFriendsFirstRender.current = false;

        setFriendsMap(new Map(
            friends.map(f => {
                return [f.userId, f];
            }),
        ))
    }, [friends])

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

    const handleSearchTextChange = (event) => {
        const value = event.target.value;

        setFilteredFriends(friends.filter(friend => friend.name.toLowerCase().includes(value.toLowerCase())));

        setSearchText(value);
    };

    return (
        <>
            <BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
                {dialogTitle}
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
                            value={searchText}
                            onChange={handleSearchTextChange}
                        />
                        <IconButton type="button" sx={{p: '10px'}} aria-label="search">
                            <SearchIcon/>
                        </IconButton>
                    </Paper>
                    <FormControl sx={{pt: 2}}>
                        <FormLabel id="demo-radio-buttons-group-label" sx={{pt: 2}}>친구</FormLabel>
                        <FormGroup>
                            {filteredFriends && filteredFriends.length > 0 &&
                                filteredFriends.map((_friend) => {
                                    return (
                                        <FormControlLabel
                                            control={<Checkbox
                                                id={_friend.userId.toString()}
                                                disabled={participatingUserIds.includes(_friend.userId.toString())}
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