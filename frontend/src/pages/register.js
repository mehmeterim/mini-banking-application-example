import { useState } from "react";
import axios from "../util/CustomAxios";
import { Link, useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();
  const [username, setUsername] = useState(null);
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);

  const onSubmit = (e) => {
    e.preventDefault();

    if (
      email === null ||
      email === "" ||
      password === null ||
      password === "" ||
      username === null ||
      username === ""
    ) {
      return;
    }

    axios
      .post("users/register", {
        username: username,
        email: email,
        password: password,
      })
      .then((res) => {
        if (res.data.success) {
          localStorage.setItem("token", res.data.data);

          navigate("/login");
        }
      })
      .catch((err) => {});
  };

  return (
    <>
      <div className="card card-md">
        <div className="card-body">
          <h2 className="h2 text-center mb-4">Create your account</h2>
          <form onSubmit={onSubmit}>
            <div className="mb-3">
              <label className="form-label">username</label>
              <input
                type="text"
                className="form-control"
                placeholder="user name"
                value={username ?? ""}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>

            <div className="mb-3">
              <label className="form-label">Email address</label>
              <input
                type="email"
                className="form-control"
                placeholder="your@email.com"
                value={email ?? ""}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            <div className="mb-3">
              <label className="form-label">Password</label>
              <input
                type="password"
                className="form-control"
                placeholder="*********"
                value={password ?? ""}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            <div className="form-footer">
              <button type="submit" className="btn btn-primary w-100">
                Register
              </button>
            </div>
          </form>
        </div>
      </div>
      <div className="text-center text-secondary mt-3">
        <Link to="/login">Login</Link>
      </div>
    </>
  );
}
