import axios from "axios";
import toast from "react-hot-toast";

const instance = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_LINK,
  withCredentials: false,
});

instance.interceptors.request.use(async function (config) {
  config.headers["Authorization"] = `Bearer ${
    localStorage.getItem("token") ?? ""
  }`;

  return config;
});

instance.interceptors.response.use(
  function (response) {
    if (response?.data?.message) {
      toast.success(response?.data?.message);
    }

    return response;
  },
  function (error) {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");

      //   store.dispatch(
      //     setUserData({
      //       isLoad: false,
      //       isLogin: false,
      //       data: {
      //         permissions: [],
      //       },
      //     })
      //   );
    }

    if (error.response?.data?.message) {
      toast.error(error.response?.data?.message);
    }

    return Promise.reject(error);
  }
);

export default instance;
