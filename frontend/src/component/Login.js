import axios from "axios";
import setAuthorizationToken from "./SetAuthorizationToken";

const Login = () => {
    axios.post("/login").then(response => {
        let token = response.data;
        localStorage.setItem("jwt", token);
        setAuthorizationToken(token);
    })
}

export default Login;