import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import Login from "./pages/login";
import Register from "./pages/register";
import Accounts from "./pages/accounts";
import Account from "./pages/account";
import CreateAccount from "./pages/createAccount";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "./util/CustomAxios";
import { setUserData } from "./redux/slices/userSlice";

function App() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { userData, trigger } = useSelector((state) => state.user);

  const [isLoading, setIsLoading] = useState(true);

  const logout = () => {
    localStorage.removeItem("token");

    dispatch(
      setUserData({
        isLoad: false,
        isLogin: false,
        data: null,
      })
    );

    navigate("/login");
  };

  useEffect(() => {
    axios
      .get("users/me")
      .then((res) => {
        if (res.data.success) {
          dispatch(
            setUserData({
              isLoad: true,
              isLogin: true,
              data: res.data.data,
            })
          );

          navigate("/accounts");
        } else {
          dispatch(
            setUserData({
              isLoad: true,
              isLogin: false,
              data: null,
            })
          );
        }

        setIsLoading(false);
      })
      .catch((err) => {
        dispatch(
          setUserData({
            isLoad: true,
            isLogin: false,
            data: null,
          })
        );

        setIsLoading(false);
      });
  }, [trigger, dispatch]);

  return (
    <div className="page">
      <div className="page-wrapper">
        <div className="page-body">
          <div className="container-xl">
            {isLoading ? (
              "loading..."
            ) : (
              <div className="row gy-3">
                {userData.isLogin && (
                  <div className="col-12">
                    <div className="row">
                      <div className="col">
                        Hello, {userData.data?.username ?? "?"}
                      </div>
                      <div className="col-auto">
                        <button
                          className="btn btn-success"
                          onClick={() => navigate("/accounts/add")}
                        >
                          Add New Account
                        </button>
                      </div>

                      <div className="col-auto">
                        <button className="btn btn-primary" onClick={logout}>
                          Logout
                        </button>
                      </div>
                    </div>
                  </div>
                )}

                <div className="col-12">
                  <Routes>
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/accounts" element={<Accounts />} />
                    <Route path="/accounts/add" element={<CreateAccount />} />
                    <Route path="/accounts/:accountId" element={<Account />} />
                    <Route path="*" element={<Navigate to={"/login"} replace />} />
                  </Routes>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
