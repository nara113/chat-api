import ForumOutlinedIcon from "@mui/icons-material/ForumOutlined";
import IconButton from "@mui/material/IconButton";
import React, {useState} from "react";
import {styled} from "@mui/material/styles";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import CloseIcon from "@mui/icons-material/Close";
import PropTypes from "prop-types";
import axios from "axios";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import SearchIcon from '@mui/icons-material/Search';
import InputAdornment from '@mui/material/InputAdornment';
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import ListItemButton from "@mui/material/ListItemButton";

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

const OpenChatDialog = ({open, handleClose}) => {
    const [searchText, setSearchText] = useState('')
    const [openChatRooms, setOpenChatRooms] = useState();

    const handleChange = (event) => {
        const value = event.target.value;

        if (value.length > 30) return;

        setSearchText(value);
    };

    const searchOpenChat = () => {
        axios.get(`/api/open-chat?searchText=${searchText}`)
            .then(r => {
                setOpenChatRooms(r.data.data)
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
                카카오톡오픈채팅
            </BootstrapDialogTitle>
            <DialogContent sx={{width: 440}}>
                <TextField
                    id="input-with-icon-textfield"
                    sx={{width: '100%', py: 1}}
                    placeholder={"채팅방 이름/소개, 태그 검색"}
                    value={searchText}
                    onChange={handleChange}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start">
                                <SearchIcon/>
                            </InputAdornment>
                        ),
                    }}
                    variant="standard"
                />
                <List>
                    {
                        openChatRooms &&
                        openChatRooms.map((_openChatRoom) => {
                            return (
                                <ListItem
                                    // secondaryAction={
                                    //     <IconButton edge="end" aria-label="delete">
                                    //         <DeleteIcon/>
                                    //     </IconButton>
                                    // }
                                >
                                    <ListItemButton sx={{p: 0}}>
                                        <ListItemText
                                            primary={_openChatRoom.name}
                                            secondary="Secondary text"
                                        />
                                    </ListItemButton>
                                </ListItem>)
                        })
                    }
                </List>
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={searchOpenChat} disabled={!searchText}>
                    검색
                </Button>
            </DialogActions>
        </BootstrapDialog>
    );
}

export default function OpenChatButton({}) {
    const currentUser = useState(JSON.parse(localStorage.getItem('user')));

    const [openChatDialogOpen, setOpenChatDialogOpen] = useState(false);
    const handleOpenChatDialogOpen = () => setOpenChatDialogOpen(true);
    const handleOpenChatDialogClose = (event, reason) => {
        if (reason && reason === "backdropClick") return;

        setOpenChatDialogOpen(false);
    }

    return (
        <>
            <IconButton color="inherit"
                        onClick={handleOpenChatDialogOpen}>
                <ForumOutlinedIcon/>
            </IconButton>
            {openChatDialogOpen && <OpenChatDialog
                open={openChatDialogOpen}
                handleClose={handleOpenChatDialogClose}
            />}
        </>
    )
}