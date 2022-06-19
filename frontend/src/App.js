import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Chat from "./component/Chat";
import React from "react";

const App = () => {
    return (
        <BrowserRouter>
            <div className="m-3">
                <Routes>
                    <Route path="/" element={<Chat/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App;
