import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Chat from "./component/Chat";
import React, {useState} from "react";
import LoginForm from "./component/LoginForm";

const App = () => {
    const [user, setUser] = useState(null);
    const authenticated = user != null;

    const logout = () => setUser(null);

    return (
        <BrowserRouter>
            <div className="m-3">
                <Routes>
                    <Route path="/" element={<Chat/>}/>
                    <Route path="/login" element={<LoginForm authenticated={authenticated}/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App;
