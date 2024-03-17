import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  trigger: false, //yeniden token kontrol etme için (useffect tetikleme useCheckLogin için)
  userData: {
    isLoad: false,
    isLogin: false,
    data: {
      id: null,
      username: null,
    },
  },
};

export const userSlice = createSlice({
  name: "user",
  initialState: initialState,
  reducers: {
    setUserData: (state, value) => {
      state.userData = value.payload;
    },
    setTrigger: (state) => {
      state.trigger = !state.trigger;
    },
  },
});

export const { setUserData, setTrigger } = userSlice.actions;

export default userSlice.reducer;
