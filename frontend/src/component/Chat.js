import React from "react";
import {MainContainer} from "@chatscope/chat-ui-kit-react";
import MyChatContainer from "./MyChatContainer";
import MySidebar from "./MySidebar";

const Chat = ({roomId}) => {

    return (
        <div style={{
            height: "600px",
            position: "relative"
        }}>
            <MainContainer responsive>
                <MySidebar/>
                <MyChatContainer roomId={1}/>

            </MainContainer>
        </div>
    )
}

export default Chat;