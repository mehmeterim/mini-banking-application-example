import { useEffect, useState } from "react";
import axios from "../util/CustomAxios";
import { useParams, useNavigate } from "react-router-dom";

export default function Account() {
  const navigate = useNavigate();
  let { accountId } = useParams();

  const [account, setAccount] = useState({ name: "", number: "", balance: 0 });

  const [number, setNumber] = useState(null);
  const [name, setName] = useState(null);

  const [transactions, setTransactions] = useState([]);

  const getDatas = () => {
    axios
      .get("accounts/" + accountId)
      .then((res) => {
        if (res.data.success) {
          setAccount(res.data.data);
          setName(res.data.data.name);
          setNumber(res.data.data.number);

          axios.get("transactions/account/" + accountId).then((res2) => {
            if (res2.data.success) {
              setTransactions(res2.data.data);
            }
          });
        }
      })
      .catch((err) => {});
  };

  const deleteAccount = () => {
    axios
      .delete("accounts/" + accountId)
      .then((res) => {
        if (res.data.success) {
          navigate("/accounts");
        }
      })
      .catch((err) => {});
  };

  const updateAccount = () => {
    axios
      .put("accounts/" + accountId, {
        number: number,
        name: name,
      })
      .then((res) => {
        if (res.data.success) {
          setAccount({ ...account, number: number, name: name });
        }
      })
      .catch((err) => {});
  };

  useEffect(() => {
    getDatas();
  }, [accountId]);

  return (
    <>
      <div className="row row-cards">
        <div className="col-md-12">
          <div className="card">
            <div className="card-header">
              <h3 className="card-title">{`${account.name} (${account.number}) - Detail`}</h3>
            </div>
            <div className="card-body">
              <div className="row gy-3">
                <div className="col-6">
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

                <div className="col-12">
                  <button
                    className="btn btn-primary w-100"
                    onClick={updateAccount}
                  >
                    Update
                  </button>
                </div>

                <div className="col-12">
                  <button
                    className="btn btn-danger w-100"
                    onClick={deleteAccount}
                  >
                    Delete
                  </button>
                </div>

                <div className="col-12">BALANCE: {account.balance}</div>
              </div>
            </div>
          </div>
        </div>

        {transactions.map((transaction) => {
          return (
            <div className="col-md-6 col-lg-3" key={transaction.id}>
              <div className="card">
                <div className="card-header">
                  <h3 className="card-title">{`${transaction.fromAccount.name} -> ${transaction.toAccount.name}`}</h3>
                </div>
                <div className="card-body">
                  <div className="row gy-3">
                    <div className="col-12">Balance: {transaction.amount}</div>
                    <div className="col-12">Status: {transaction.status}</div>
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
