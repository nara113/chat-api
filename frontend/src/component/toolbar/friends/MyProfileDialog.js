import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import Box from '@mui/material/Box';
import ChatBubbleIcon from '@mui/icons-material/ChatBubble';
import EditIcon from '@mui/icons-material/Edit';
import Avatar from '@mui/material/Avatar';
import Divider from '@mui/material/Divider';
import {useState} from "react";
import axios from "axios";
import IconButton from "@mui/material/IconButton";

export default function MyProfileDialog({open, handleClose, currentUser}) {
    const [profileUrl, setProfileUrl] = useState(currentUser.profileUrl);

    const handleChange = e => {
        if (e.target.files) {
            const uploadFile = e.target.files[0]

            const requestProfile = new FormData()
            requestProfile.append('image', uploadFile)

            axios.post("/api/v1/upload/profile-image", requestProfile)
                .then((r) => {
                    setProfileUrl(r.data.data);
                })
        }
    };

    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            {/*<DialogTitle id="alert-dialog-title">*/}
            {/*</DialogTitle>*/}
            <DialogContent
                sx={{
                    height: 500,
                    width: 350,
                    backgroundColor: 'gray'
                }}
            >
                <Box
                    noValidate
                    component="form"
                    sx={{
                        display: 'flex',
                        // flexDirection: 'column',
                        flexWrap: 'wrap',
                        m: 'auto',
                        alignContent: 'flex-end',
                        justifyContent: 'center',
                        width: '100%',
                        height: '100%',
                    }}
                >
                    <div>
                        <div style={{display: "flex", justifyContent: 'center'}}>
                            <Avatar sx={{width: 100, height: 100, borderRadius: 9}}
                                    src={profileUrl}/>
                        </div>
                        <h3 style={{
                            color: "white",
                            mt: 0,
                            display: "flex",
                            justifyContent: 'center'
                        }}>{currentUser.name}</h3>
                        <p style={{
                            color: "white",
                            mt: 0,
                            display: "flex",
                            justifyContent: 'center'
                        }}>{currentUser.statusMessage}</p>
                    </div>
                </Box>
            </DialogContent>
            <Divider/>
            <DialogActions sx={{
                backgroundColor: 'gray',
                height: 100
            }}>
                <Box
                    sx={{
                        display: 'flex',
                        justifyContent: 'space-around',
                        width: '100%',
                        height: '100%',
                    }}
                >
                    <IconButton sx={{
                        color: 'white',
                        display: 'flex',
                        flexDirection: 'column',
                    }}
                                aria-label="upload picture" component="label">
                        <ChatBubbleIcon/>
                        <span style={{fontSize: 13}}>나와의 채팅</span>
                    </IconButton>
                    <IconButton sx={{
                        color: 'white',
                        display: 'flex',
                        flexDirection: 'column',
                    }}
                                aria-label="upload picture"
                                component="label">
                        <input hidden accept="image/*"
                               onChange={handleChange}
                               type="file"/>
                        <EditIcon/>
                        <span style={{fontSize: 13}}>프로필 편집</span>
                    </IconButton>
                </Box>
            </DialogActions>
        </Dialog>
    );
}
