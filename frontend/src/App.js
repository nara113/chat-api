import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import React, {useState} from "react";
import SignIn from "./component/SignIn";
import ChatRoom from "./component/ChatRoom";
import AppContainer from "./component/AppContainer";

import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";


const App = () => {
    return (
        <BrowserRouter>
            <div className="m-3">
                <Routes>
                    <Route path="/" element={<SignIn/>}/>
                    <Route path="/chat/room" element={<AppContainer/>}/>
                    {/*<Route path="/chat/room" element={<ChatRoom/>}/>*/}
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App;
