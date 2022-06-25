import axios from "axios";
import setAuthorizationToken from "./SetAuthorizationToken";
import React from "react";

const Login = (data) => {

    axios.post("/api/v1/users/login", data)
        .then(response => {
            const data = response.data.data;

            localStorage.setItem("jwt", data.token)
            localStorage.setItem('user', JSON.stringify(data.user))
            setAuthorizationToken(data.token);
        })
}

export default Login;