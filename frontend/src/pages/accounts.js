import { useEffect, useState } from "react";
import axios from "../util/CustomAxios";
import { Link } from "react-router-dom";

export default function Accounts() {
  const [accounts, setAccounts] = useState([]);

  const [number, setNumber] = useState(null);
  const [name, setName] = useState(null);

  const [fromAccountId, setFromAccountId] = useState(null);
  const [toAccountId, setToAccountId] = useState(null);
  const [amount, setAmount] = useState(0);

  const getDatas = () => {
    axios
      .get("accounts", {
        params: {
          name: name,
          number: number,
        },
      })
      .then((res) => {
        if (res.data.success) {
          setAccounts(res.data.data);

          if (res.data.data.length > 0) {
            setFromAccountId(res.data.data[0].id);
            setToAccountId(res.data.data[0].id);
          }
        }
      })
      .catch((err) => {});
  };

  const transfer = () => {
    axios
      .post("transactions/transfer", {
        fromAccountId: fromAccountId,
        toAccountId: toAccountId,
        amount: amount,
      })
      .then((res) => {
        if (res.data.success) {
          getDatas();
        }
      })
      .catch((err) => {});
  };

  useEffect(() => {
    getDatas();
  }, [name, number]);

  return (
    <>
      <div className="row row-cards">
        {accounts.length > 0 && (
          <div className="col-md-12">
            <div className="card">
              <div className="card-header">
                <h3 className="card-title">Transfer</h3>
              </div>
              <div className="card-body">
                <div className="row gy-3">
                  <div className="col-3">
                    <label>From Account</label>
                    <select
                      className="form-select"
                      value={fromAccountId}
                      onChange={(e) => setFromAccountId(e.target.value)}
                    >
                      {accounts.map((account) => {
                        return (
                          <option value={account.id} key={account.id}>
                            {account.name}
                          </option>
                        );
                      })}
                    </select>
                  </div>
                  <div className="col-3">
                    <label>To Account</label>
                    <select
                      className="form-select"
                      value={toAccountId}
                      onChange={(e) => setToAccountId(e.target.value)}
                    >
                      {accounts.map((account) => {
                        return (
                          <option value={account.id} key={account.id}>
                            {account.name}
                          </option>
                        );
                      })}
                    </select>
                  </div>
                  <div className="col-3">
                    <label>Amount</label>
                    <input
                      type="number"
                      className="form-control"
                      value={amount}
                      onChange={(e) => setAmount(e.target.value)}
                    />
                  </div>
                  <div className="col-3">
                    <label>&nbsp;</label>
                    <button
                      className="btn btn-primary w-100"
                      onClick={transfer}
                    >
                      Transfer
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        <div className="col-md-12">
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">Filters</h3>
            </div>
            <div className="card-body">
              <div className="row gy-3">
                <div className="col-6">
                  <label>Number</label>
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
                <div className="col-6">
                  <label>Name</label>
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
              </div>
            </div>
          </div>
        </div>

        {accounts.map((account) => {
          return (
            <div className="col-md-6 col-lg-3" key={account.id}>
              <div className="card">
                <div className="card-header">
                  <h3 className="card-title">{`${account.name} (${account.number})`}</h3>
                </div>
                <div className="card-body">
                  <div className="row gy-3">
                    <div className="col-12">Balance: {account.balance}</div>
                    <div className="col-12">
                      <Link
                        className="btn btn-primary"
                        to={`/accounts/${account.id}`}
                      >
                        Detail
                      </Link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </>
  );
}
