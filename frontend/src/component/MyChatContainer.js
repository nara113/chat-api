import {
    Avatar,
    ChatContainer,
    ConversationHeader, Message, MessageInput, MessageList
} from "@chatscope/chat-ui-kit-react";
import React, {useEffect, useRef, useState} from "react";
import * as StompJs from "@stomp/stompjs";
import * as SockJS from "sockjs-client";
import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";

const MyChatContainer = ({room, getUserName}) => {
    const img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAATpJREFUWEdjvPm+8T/DEACMow6lciyNhiiVA5RhNERHQ5TaIUBt80bT6GiIUjsEqG0e0WlUhb+SgYmRFWz/i2/rGT79uoziFkW+PAZWJgGw2Pufxxlef9+N1a3CHA4Mwhx2YLk//z4z3PvUT5SfiHaoqkA1AyMDM9jQl982MXz8dQHFAiW+IgYWJh6w2IefZxhefd+G1QGinC4MguxWYLm//78x3P3YM+rQ0RDFlwaGdxqF+By9B8MID5BBk5kIZdNRhxIKIZA8cjkKKuy//rmNok2c04uBiZFjtBwd3rl+tArFk1mGd9Rja+Yp8RUysDDxQnP9KYZX33dgDR8RDkcGIQ5baOvpK8Pdj73EFDqj/XqiQokURUSnUVIMpYXaUYdSO1RHQ3Q0RKkdAtQ2bzSNjoYotUOA2uYBAI6umQqSmDikAAAAAElFTkSuQmCC";


    const client = useRef({});
    const [chatMessages, setChatMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [senderId, setSenderId] = useState();

    useEffect(() => {
        connect();
        setSenderId(Math.floor(Math.random() * 101));

        return () => disconnect();
    }, [room]);

    const connect = () => {
        client.current = new StompJs.Client({
            webSocketFactory: () => new SockJS("/ws/chat"),
            connectHeaders: {
                "auth-token": "spring-chat-auth-token",
            },
            debug: function (str) {
                console.log(str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                subscribe();
            },
            onStompError: (frame) => {
                console.error(frame);
            },
        });

        client.current.activate();
    };

    const disconnect = () => {
        client.current.deactivate();
    };

    const subscribe = () => {
        client.current.subscribe(`/topic/chat/room/${room.roomId}`, ({body}) => {
            setChatMessages((_chatMessages) => [..._chatMessages, JSON.parse(body)]);
            console.log('body', body);
        });
    };

    const publish = (message) => {
        if (!client.current.connected) {
            return;
        }

        client.current.publish({
            destination: "/app/chat/message",
            body: JSON.stringify({roomId: room.roomId, sender: senderId, message}),
        });

        setMessage("");
    };

    return (
        <ChatContainer>
            <ConversationHeader>
                <ConversationHeader.Back/>
                <Avatar src={img} name="Zoe"/>
                <ConversationHeader.Content userName={getUserName(room)} />
            </ConversationHeader>
            <MessageList>
                {chatMessages && chatMessages.length > 0 &&
                chatMessages.map((_chatMessage, index) => (
                    _chatMessage.sender == senderId
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
                        }}/>
                ))
                }
            </MessageList>
            <MessageInput placeholder="Type message here"
                          value={message}
                          onSend={() => publish(message)}
                          onChange={val => setMessage(val)}/>
        </ChatContainer>
    )
}

export default MyChatContainer;