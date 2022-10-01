import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import Box from '@mui/material/Box';
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import ChatBubbleIcon from '@mui/icons-material/ChatBubble';
import EditIcon from '@mui/icons-material/Edit';
import Avatar from '@mui/material/Avatar';
import Divider from '@mui/material/Divider';
import {useState} from "react";
import axios from "axios";

export default function MyProfileDialog({open, handleClose, currentUser}) {
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
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            {/*<DialogTitle id="alert-dialog-title">*/}
            {/*</DialogTitle>*/}
            <DialogContent
                sx={{height: 500,
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
                        <Avatar sx={{ width: 100, height: 100, borderRadius: 9 }}
                                src={currentUser.profileUrl} />
                        <h3 style={{color: "white", mt: 0,}}>{currentUser.name}</h3>
                    </div>
                </Box>
            </DialogContent>
            <Divider />
            <DialogActions sx={{
                backgroundColor: 'gray'
            }}>
                <Box sx={{ width: '100%',
                    borderTop: '' }}>
                    <BottomNavigation
                        showLabels
                        sx={{ width: '100%',
                            backgroundColor: 'inherit',
                            borderTop: '' }}
                    >
                        <BottomNavigationAction label="나와의 채팅"
                                                sx={{color: 'white'}}
                                                icon={<ChatBubbleIcon />} />
                        <BottomNavigationAction label="프로필 편집"
                                                sx={{color: 'white'}}
                                                icon={<EditIcon />}
                        />
                    </BottomNavigation>
                </Box>
            </DialogActions>
        </Dialog>
    );
}
