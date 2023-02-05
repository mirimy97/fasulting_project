import { createSlice } from "@reduxjs/toolkit";
//스토어가 식당이라고 하면 리듀서는 접시 같은 놈
export const userSlice = createSlice({
  name: "user", //리듀서 이름
  initialState: {
    userEmail: "",
    userPwd: "",
  }, //데이터 초기값
  reducers: {
    //상태가 변하면 어떻게 실행될지
    //로그인 성공시
    loginUser: (state, action) => {
      //state는 초기값의 value를 가져오고 actions안에 payload랑 type 이라는 친구가 있는데 우리가 바꾸고 싶은 데이터를 원하는 곳에다가 넘겨주는 역할
      state.userEmail = action.payload;
      state.userPwd = action.payload;
      return state;
    },
    clearUser: (state) => {
      state.userEmail = "";
      state.userName = "";
      state.userBirth = "";
      state.userNumber = "";
      return state;
    },
  },
});

export const { loginUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
