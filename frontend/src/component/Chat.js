import axios from "axios";
import React, {useEffect, useRef, useState} from "react";
import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";
import {
    Sidebar,
    Search,
    MainContainer,
    ChatContainer,
    MessageList,
    Message,
    MessageInput,
    ConversationList,
    Conversation,
    Avatar,
    ConversationHeader,
    VoiceCallButton,
    VideoCallButton,
    EllipsisButton,
    MessageSeparator,
    TypingIndicator
} from "@chatscope/chat-ui-kit-react";
import * as StompJs from "@stomp/stompjs";
import * as SockJS from "sockjs-client";

const Chat = ({roomId}) => {
    const client = useRef({});
    const [chatMessages, setChatMessages] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        connect();

        return () => disconnect();
    }, []);

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
        client.current.subscribe(`/topic/chat/room/1`, ({body}) => {
            setChatMessages((_chatMessages) => [..._chatMessages, JSON.parse(body)]);
        });
    };

    const publish = (message) => {
        if (!client.current.connected) {
            return;
        }

        client.current.publish({
            destination: "/app/chat/message",
            body: JSON.stringify({type: 'TALK', roomId: 1, message}),
        });

        setMessage("");
    };
    const img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAATpJREFUWEdjvPm+8T/DEACMow6lciyNhiiVA5RhNERHQ5TaIUBt80bT6GiIUjsEqG0e0WlUhb+SgYmRFWz/i2/rGT79uoziFkW+PAZWJgGw2Pufxxlef9+N1a3CHA4Mwhx2YLk//z4z3PvUT5SfiHaoqkA1AyMDM9jQl982MXz8dQHFAiW+IgYWJh6w2IefZxhefd+G1QGinC4MguxWYLm//78x3P3YM+rQ0RDFlwaGdxqF+By9B8MID5BBk5kIZdNRhxIKIZA8cjkKKuy//rmNok2c04uBiZFjtBwd3rl+tArFk1mGd9Rja+Yp8RUysDDxQnP9KYZX33dgDR8RDkcGIQ5baOvpK8Pdj73EFDqj/XqiQokURUSnUVIMpYXaUYdSO1RHQ3Q0RKkdAtQ2bzSNjoYotUOA2uYBAI6umQqSmDikAAAAAElFTkSuQmCC";

    return (
        <div style={{
            height: "600px",
            position: "relative"
        }}>
            <MainContainer responsive>
                <Sidebar position="left" scrollable={false}>
                    <Search placeholder="Search..."/>
                    <ConversationList>
                        <Conversation name="Lilly" lastSenderName="Lilly" info="Yes i can do it for you">
                            <Avatar src={img} name="Lilly" status="available"/>
                        </Conversation>

                        <Conversation name="Joe" lastSenderName="Joe" info="Yes i can do it for you">
                            <Avatar src={img} name="Joe" status="dnd"/>
                        </Conversation>

                        <Conversation name="Emily" lastSenderName="Emily" info="Yes i can do it for you" unreadCnt={3}>
                            <Avatar src={img} name="Emily" status="available"/>
                        </Conversation>

                        <Conversation name="Kai" lastSenderName="Kai" info="Yes i can do it for you" unreadDot>
                            <Avatar src={img} name="Kai" status="unavailable"/>
                        </Conversation>

                        <Conversation name="Akane" lastSenderName="Akane" info="Yes i can do it for you">
                            <Avatar src={img} name="Akane" status="eager"/>
                        </Conversation>

                        <Conversation name="Eliot" lastSenderName="Eliot" info="Yes i can do it for you">
                            <Avatar src={img} name="Eliot" status="away"/>
                        </Conversation>

                        <Conversation name="Zoe" lastSenderName="Zoe" info="Yes i can do it for you">
                            <Avatar src={img} name="Zoe" status="dnd"/>
                        </Conversation>

                        <Conversation name="Patrik" lastSenderName="Patrik" info="Yes i can do it for you">
                            <Avatar src={img} name="Patrik" status="invisible"/>
                        </Conversation>
                    </ConversationList>
                </Sidebar>

                <ChatContainer>
                    <ConversationHeader>
                        <ConversationHeader.Back/>
                        <Avatar src={img} name="Zoe"/>
                        <ConversationHeader.Content userName="Zoe" info="Active 10 mins ago"/>
                        <ConversationHeader.Actions>
                            <VoiceCallButton/>
                            <VideoCallButton/>
                            <EllipsisButton orientation="vertical"/>
                        </ConversationHeader.Actions>
                    </ConversationHeader>
                    <MessageList>
                        <MessageSeparator content="Saturday, 30 November 2019"/>
                        <Message model={{
                            message: "Hello my friend2",
                            sentTime: "15 mins ago",
                            sender: "Zoe",
                            direction: "incoming",
                            position: "single"
                        }}>
                            <Avatar src={img} name="Zoe"/>
                        </Message>
                        <Message model={{
                            message: "Hello my friend2",
                            sentTime: "15 mins ago",
                            sender: "Patrik",
                            direction: "outgoing",
                            position: "single"
                        }}>
                        </Message>
                        <Message model={{
                            message: "Hello my friend",
                            sentTime: "15 mins ago",
                            sender: "Zoe",
                            direction: "incoming",
                            position: "first"
                        }} avatarSpacer/>
                        <Message model={{
                            message: "Hello my friend",
                            sentTime: "15 mins ago",
                            sender: "Zoe",
                            direction: "incoming",
                            position: "normal"
                        }} avatarSpacer/>
                        <Message model={{
                            message: "Hello my friend",
                            sentTime: "15 mins ago",
                            sender: "Zoe",
                            direction: "incoming",
                            position: "normal"
                        }} avatarSpacer/>
                        <Message model={{
                            message: "Hello my friend4",
                            sentTime: "15 mins ago",
                            sender: "Zoe",
                            direction: "incoming",
                            position: "last"
                        }}>
                            <Avatar src={img} name="Zoe"/>
                        </Message>
                        <Message model={{
                            message: "Hello my friend",
                            sentTime: "15 mins ago",
                            sender: "Patrik",
                            direction: "outgoing",
                            position: "first"
                        }}/>
                        <Message model={{
                            message: "Hello my friend",
                            sentTime: "15 mins ago",
                            sender: "Patrik",
                            direction: "outgoing",
                            position: "normal"
                        }}/>
                        <Message model={{
                            message: "Hello my friend",
                            sentTime: "15 mins ago",
                            sender: "Patrik",
                            direction: "outgoing",
                            position: "last"
                        }}/>
                        {chatMessages && chatMessages.length > 0 &&
                            chatMessages.map((_chatMessage, index) => (
                                <Message model={{
                                    message: _chatMessage.message,
                                    sentTime: "15 mins ago",
                                    sender: "Patrik",
                                    direction: "outgoing",
                                    position: "last"
                                }}/>
                            ))
                        }
                    </MessageList>
                    <MessageInput placeholder="Type message here" value={message}
                                  onSend={() => publish(message)}
                                  onChange={val => setMessage(val)}/>
                </ChatContainer>
            </MainContainer>
        </div>
    )
}

export default Chat;