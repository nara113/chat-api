import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Chat from "./component/Chat";
import React from "react";
import Sock from "./component/Sock";

const App = () => {
    return (
        <BrowserRouter>
            <div className="m-3">
                <Routes>
                    <Route path="/" element={<Chat/>}/>
                    <Route path="/sock" element={<Sock/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App;
