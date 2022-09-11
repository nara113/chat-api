import Toolbar from "@mui/material/Toolbar";
import Grid from "@mui/material/Grid";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import React, {useEffect, useState} from "react";
import {styled} from "@mui/material/styles";
import Paper from "@mui/material/Paper";
import axios from "axios";

const StyledPaper = styled(Paper)(({theme}) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(2),
    maxWidth: 400,
    color: theme.palette.text.primary,
}));

export default function ChatFriends() {
    const [friends, setFriends] = useState()

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
        <Box sx={{flexGrow: 1, overflow: 'hidden', px: 3}}>
            <Toolbar/>
            {friends && friends.length > 0 &&
                friends.map((_friend) => {
                    return (
                        <StyledPaper
                            sx={{
                                my: 1,
                                mx: 'auto',
                                p: 2,
                                width: 800
                            }}
                        >
                            <Grid container wrap="nowrap" spacing={2}>
                                <Grid item>
                                    <Avatar />
                                </Grid>
                                <Grid item xs={12} sm container zeroMinWidth>
                                    <Grid item xs zeroMinWidth>
                                        <Typography
                                            noWrap>{_friend.name}</Typography>
                                        <Typography variant="body2" color="text.secondary">
                                            {_friend.statusMessage}
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </StyledPaper>
                    )
                })
            }
        </Box>
    )
}