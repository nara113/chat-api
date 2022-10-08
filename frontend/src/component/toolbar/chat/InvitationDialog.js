import React, {useState} from "react";
import ChatParticipants from "../../commons/ChatParticipants";
import {styled} from "@mui/material/styles";
import Dialog from "@mui/material/Dialog";
import axios from "axios";

const BootstrapDialog = styled(Dialog)(({theme}) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

export default function InvitationDialog({
                                             invitationDialogOpen,
                                             handleInvitationDialogClose,
                                             currentRoom
                                         }) {
    const [participantUsers, setParticipantUsers] = useState([]);

    const inviteUsers = () => {
        const invitedUserIds = participantUsers.map(user => user.userId);

        axios.post(`/api/rooms/${currentRoom.roomId}/users`, invitedUserIds)
            .then(r => {
                handleInvitationDialogClose();
                window.location.reload(false);
            })
    }

    return (
        <BootstrapDialog
            onClose={handleInvitationDialogClose}
            aria-labelledby="customized-dialog-title"
            open={invitationDialogOpen}
        >
            <ChatParticipants dialogTitle={'대화상대 초대'}
                              handleClose={handleInvitationDialogClose}
                              participantUsers={participantUsers}
                              setParticipantUsers={setParticipantUsers}
                              handleNext={inviteUsers}/>
        </BootstrapDialog>
    )
}