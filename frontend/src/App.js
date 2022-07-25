import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Chat from "./component/Chat";
import React, {useState} from "react";
import SignIn from "./component/SignIn";
import ChatRoom from "./component/ChatRoom";


const App = () => {
    return (
        <BrowserRouter>
            <div className="m-3">
                <Routes>
                    <Route path="/" element={<SignIn/>}/>
                    <Route path="/chat" element={<Chat/>}/>
                    <Route path="/chat/room" element={<ChatRoom/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App;
