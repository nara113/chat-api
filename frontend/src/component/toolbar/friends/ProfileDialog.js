import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Divider from '@mui/material/Divider';

export default function ProfileDialog({open, handleClose, user}) {

    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
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
                                    src={user.profileUrl}/>
                        </div>
                        <h3 style={{
                            color: "white",
                            mt: 0,
                            display: "flex",
                            justifyContent: 'center'
                        }}>{user.name}</h3>
                        <p style={{
                            color: "white",
                            mt: 0,
                            display: "flex",
                            justifyContent: 'center'
                        }}>{user.statusMessage}</p>
                    </div>
                </Box>
            </DialogContent>
            <Divider/>
            <DialogActions sx={{
                backgroundColor: 'gray',
                height: 100
            }}>
            </DialogActions>
        </Dialog>
    );
}
