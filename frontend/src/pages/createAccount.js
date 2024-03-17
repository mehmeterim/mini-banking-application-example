import { useState } from "react";
import axios from "../util/CustomAxios";
import { useNavigate } from "react-router-dom";

export default function CreateAccount() {
  const navigate = useNavigate();
  const [number, setNumber] = useState(null);
  const [name, setName] = useState(null);
  const [balance, setBalance] = useState(0);

  const createAccount = () => {
    axios
      .post("accounts", {
        number: number,
        name: name,
        balance: balance,
      })
      .then((res) => {
        if (res.data.success) {
          navigate("/accounts");
        }
      })
      .catch((err) => {});
  };

  return (
    <>
      <div className="row row-cards">
        <div className="col-md-12">
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Create Account</h3>
            </div>
            <div className="card-body">
              <div className="row gy-3">
                <div className="col-3">
                  <input
                    type="text"
                    className="form-control"
                    value={number}
                    placeholder="number"
                    onChange={(e) =>
                      setNumber(
                        e.target.value.trim() === "" ? null : e.target.value
                      )
                    }
                  />
                </div>
                <div className="col-3">
                  <input
                    type="text"
                    className="form-control"
                    value={name}
                    placeholder="name"
                    onChange={(e) =>
                      setName(
                        e.target.value.trim() === "" ? null : e.target.value
                      )
                    }
                  />
                </div>
                <div className="col-3">
                  <input
                    type="number"
                    className="form-control"
                    value={balance}
                    placeholder="name"
                    onChange={(e) => setBalance(e.target.value)}
                  />
                </div>

                <div className="col-3">
                  <button
                    className="btn btn-primary w-100"
                    onClick={createAccount}
                  >
                    Create
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
