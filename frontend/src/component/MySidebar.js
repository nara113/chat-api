import {AvatarGroup, Avatar, Conversation, ConversationList, Message, Search, Sidebar} from "@chatscope/chat-ui-kit-react";
import React, {useEffect, useState} from "react";
import axios from "axios";

const MySidebar = () => {
    const img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAATpJREFUWEdjvPm+8T/DEACMow6lciyNhiiVA5RhNERHQ5TaIUBt80bT6GiIUjsEqG0e0WlUhb+SgYmRFWz/i2/rGT79uoziFkW+PAZWJgGw2Pufxxlef9+N1a3CHA4Mwhx2YLk//z4z3PvUT5SfiHaoqkA1AyMDM9jQl982MXz8dQHFAiW+IgYWJh6w2IefZxhefd+G1QGinC4MguxWYLm//78x3P3YM+rQ0RDFlwaGdxqF+By9B8MID5BBk5kIZdNRhxIKIZA8cjkKKuy//rmNok2c04uBiZFjtBwd3rl+tArFk1mGd9Rja+Yp8RUysDDxQnP9KYZX33dgDR8RDkcGIQ5baOvpK8Pdj73EFDqj/XqiQokURUSnUVIMpYXaUYdSO1RHQ3Q0RKkdAtQ2bzSNjoYotUOA2uYBAI6umQqSmDikAAAAAElFTkSuQmCC";

    const [rooms, setRooms] = useState();

    const getAllRooms = () => {
        axios.get("/api/v1/rooms").then(response => {
            console.log(response);
            setRooms(response.data);
        })
    }

    useEffect(() => {
        getAllRooms();
    }, []);
    return (
        <Sidebar position="left" scrollable={false}>
            <Search placeholder="Search..."/>
            <ConversationList>
                {rooms && rooms.length > 0 &&
                rooms.map(_room => (
                    <Conversation lastSenderName="You"
                                  name={_room.users.map(_user => _user.name).join(', ')}
                                  info="Yes, i can do it for you"
                                  unreadCnt={3}>
                        <AvatarGroup size="sm">
                            {_room.users.map(_user => <Avatar src={img} name={_user.name} />)}
                        </AvatarGroup>
                    </Conversation>
                ))
                }
            </ConversationList>
        </Sidebar>
    )
}

export default MySidebar;