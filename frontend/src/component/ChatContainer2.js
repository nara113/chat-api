import {
    Avatar,
    ChatContainer,
    ConversationHeader,
    MainContainer,
    Message,
    MessageInput,
    MessageList,
    MessageSeparator
} from "@chatscope/chat-ui-kit-react";
import React, {useEffect, useState} from "react";
import axios from "axios";

const MyChatContainer2 = ({client, room, roomUsers, currentUserId, newMessage}) => {
    const img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAATpJREFUWEdjvPm+8T/DEACMow6lciyNhiiVA5RhNERHQ5TaIUBt80bT6GiIUjsEqG0e0WlUhb+SgYmRFWz/i2/rGT79uoziFkW+PAZWJgGw2Pufxxlef9+N1a3CHA4Mwhx2YLk//z4z3PvUT5SfiHaoqkA1AyMDM9jQl982MXz8dQHFAiW+IgYWJh6w2IefZxhefd+G1QGinC4MguxWYLm//78x3P3YM+rQ0RDFlwaGdxqF+By9B8MID5BBk5kIZdNRhxIKIZA8cjkKKuy//rmNok2c04uBiZFjtBwd3rl+tArFk1mGd9Rja+Yp8RUysDDxQnP9KYZX33dgDR8RDkcGIQ5baOvpK8Pdj73EFDqj/XqiQokURUSnUVIMpYXaUYdSO1RHQ3Q0RKkdAtQ2bzSNjoYotUOA2uYBAI6umQqSmDikAAAAAElFTkSuQmCC";

    const [chatMessages, setChatMessages] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        axios.get(`/api/v1/rooms/${room.roomId}/messages`)
            .then(res => {
                setChatMessages(res.data.data);
            })

        client.current.publish({
            destination: "/app/chat/enter",
            body: JSON.stringify({roomId: room.roomId, senderId: currentUserId}),
        });
    }, [room]);

    useEffect(() => {
        if (!newMessage) {
            return;
        }

        setChatMessages((_chatMessages) => [..._chatMessages, newMessage]);
    }, [newMessage])

    const publish = (message) => {
        if (!client.current.connected) {
            return;
        }

        client.current.publish({
            destination: "/app/chat/message/users",
            body: JSON.stringify({roomId: room.roomId, senderId: currentUserId, message}),
        });

        setMessage("");
    };

    return (
        <div style={{
            width: "600px",
            height: "400px",
            position: "relative"
        }}>
            <MainContainer responsive>
                <ChatContainer>
                    <ConversationHeader>
                        <ConversationHeader.Back/>
                        <Avatar src={img} name="Zoe"/>
                        <ConversationHeader.Content userName={roomUsers.map(_user => _user.name).join(', ')}/>
                    </ConversationHeader>
                    <MessageList>
                        {chatMessages && chatMessages.length > 0 &&
                            chatMessages.map((_chatMessage, index) => (
                                _chatMessage.chatType === 'ENTER'
                                    ? <MessageSeparator
                                        content="Content from property">enter {_chatMessage.senderId}</MessageSeparator>
                                    :
                                    (_chatMessage.senderId === currentUserId
                                        ? <Message key={index}
                                                   model={{
                                                       message: _chatMessage.message,
                                                       sentTime: "15 mins ago",
                                                       sender: "Patrik",
                                                       direction: "outgoing",
                                                       position: "last"
                                                   }}/>
                                        : <Message key={index}
                                                   model={{
                                                       message: _chatMessage.message,
                                                       sentTime: "15 mins ago",
                                                       sender: "Patrik",
                                                       direction: "incoming",
                                                       position: "last"
                                                   }}/>)
                            ))
                        }
                    </MessageList>
                    <MessageInput placeholder="Type message here"
                                  value={message}
                                  onSend={() => publish(message)}
                                  onChange={val => setMessage(val)}/>
                </ChatContainer>
            </MainContainer>
        </div>
    )
}

export default MyChatContainer2;