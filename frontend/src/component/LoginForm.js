import React, {useState} from "react";
import login from "./Login";
import { useNavigate } from "react-router-dom";

const LoginForm = () => {
    let navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleClick = () => {
        try {
            login({ email, password });
            navigate('/', {state: email});
        } catch (e) {
            alert("Failed to login");
            setEmail("");
            setPassword("");
        }
    };

    // const {from} = location.state || {from: {pathname: "/"}};
    // if (authenticated) return <Redirect to={from}/>;

    return (
        <>
            <h1>Login</h1>
            <input
                value={email}
                onChange={({target: {value}}) => setEmail(value)}
                type="text"
                placeholder="email"
            />
            <input
                value={password}
                onChange={({target: {value}}) => setPassword(value)}
                type="password"
                placeholder="password"
            />
            <button onClick={handleClick}>Login</button>
        </>
    );
}

export default LoginForm;