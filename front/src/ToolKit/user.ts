import { createSlice, createAsyncThunk, AsyncThunk } from '@reduxjs/toolkit';
import {
  listMessages,
  login_user,
  readMessages,
  register_user,
  reissueToken,
  sendMessages,
  deleteMessages,
  myInfo,
} from '../API/index';
import { RegisterType, LoginType, Token, TokenCheck, USER_TYPE } from './userType';
import { ResRegister, ResLogin, PayloadSuccessType, PayloadFailType, ResSendMessage } from './axiosType';
import { ReducerType } from '../rootReducer';
import { MESSAGE_TYPE, ReadMessageType, SendMessageType } from './MessageTypes';

export const register = createAsyncThunk('user/REGISTER', async (user: RegisterType, { dispatch, rejectWithValue }) => {
  try {
    const { data }: PayloadSuccessType<ResRegister> = await register_user(user);

    return {
      type: USER_TYPE.USER_REGISTER,
      payload: data,
    };
  } catch (err: any) {
    const error: PayloadFailType = err.response.data;

    const obj = {
      ...error,
      type: USER_TYPE.USER_REGISTER,
    };

    // errorHandler(err.response.data.code);
    // console.log('Err', err.response.data.code);
    console.log(obj);

    return rejectWithValue(obj);
  }
});

export const login = createAsyncThunk('LOGIN', async (user: LoginType, { rejectWithValue }) => {
  try {
    const { data }: PayloadSuccessType<ResLogin> = await login_user(user);

    saveLocalStorage(data.data);

    return {
      type: USER_TYPE.USER_LOGIN,
      payload: data,
    };
  } catch (err: any) {
    const error: PayloadFailType = err.response.data;

    const obj = {
      ...error,
      type: USER_TYPE.USER_LOGIN,
    };
    return rejectWithValue(obj);
  }
});

export const infoMy = createAsyncThunk('MY_INFO', async (arg, { rejectWithValue }) => {
  try {
    const {
      data: { data },
    } = await myInfo();

    return {
      type: USER_TYPE.USER_LOGIN,
      payload: data,
    };
  } catch (err: any) {
    const error: PayloadFailType = err.response.data;

    const obj = {
      ...error,
      type: USER_TYPE.USER_LOGIN,
    };
    return rejectWithValue(obj);
  }
});

export const checkToken = createAsyncThunk('CHECK_TOKEN', async (arg, { rejectWithValue }) => {
  const obj: TokenCheck = {
    type: USER_TYPE.USER_CHECK_TOKEN,
    success: false,
    message: '',
  };
  try {
    const date = new Date();
    const expiresIn = parseInt(localStorage.getItem('accessTokenExpiresIn') as string);
    const now = date.getTime();
    const diffTime = expiresIn - now;
    // const diffTime = 50000;

    window.addEventListener('storage', (e) => {
      if (e.isTrusted) {
        // accessTokenExpiresIn값 변경
        localStorage.setItem('accessTokenExpiresIn', '9999999999999999999999999999');
        // 페이지 리로드
        window.location.reload();
      }
    });

    if (expiresIn) {
      // 로컬 스토리지에 accessTokenExpiresIn 시간 조작하면 인증된 토큰으로 나옴
      if (diffTime > 1800000) {
        obj.message = '비정상적인 토큰';
        return obj;
      }
      if (diffTime < 59405 && diffTime > 0) {
        const accessToken = localStorage.getItem('accessToken') as string;
        const refreshToken = localStorage.getItem('refreshToken') as string;
        const { data }: PayloadSuccessType<ResLogin> = await reissueToken({ accessToken, refreshToken });

        saveLocalStorage(data.data);

        obj.success = true;
        obj.type = USER_TYPE.USER_REISSUE;
        obj.message = '토큰 갱신 성공';
        return obj;
      }
      if (diffTime <= 0) {
        logout();
        obj.message = '만료된 토큰';
        return obj;
      }
    } else {
      obj.message = '존재하지 않은 토큰';
      return obj;
    }
    obj.success = true;
    obj.message = '유효한 토큰';
    return obj;
  } catch (err: any) {
    const error: PayloadFailType = err.response.data;

    console.log('err', error);

    obj.err = error;
    return rejectWithValue(obj);
  }
});

const saveLocalStorage = (token: Token) => {
  localStorage.setItem('accessToken', token.accessToken);
  localStorage.setItem('refreshToken', token.refreshToken);
  localStorage.setItem('accessTokenExpiresIn', JSON.stringify(token.accessTokenExpiresIn));
};

interface AuthType {
  type: string;
  payload: {
    type: string;
    success: boolean;
    message: string;
  };
}

export interface InitialState {
  user: {
    type: USER_TYPE.USER_REGISTER | USER_TYPE.USER_LOGIN | USER_TYPE.USER_LOGOUT;
    email: string;
    grade: number;
    id: number;
    nickname: string;
  };
  status: string;
  auth: AuthType;
  message: any;
}

const State: InitialState = {
  user: {
    type: USER_TYPE.USER_LOGOUT,
    email: '',
    grade: 0,
    id: 0,
    nickname: '',
  },
  status: '',
  auth: {
    type: '',
    payload: {
      type: '',
      success: false,
      message: '',
    },
  },
  message: {
    type: '',
    payload: {
      type: '',
      message: '',
      number: 0,
      payload: {
        message: '',
        status: 0,
        data: [],
      },
    },
  },
};

const user = createSlice({
  name: 'user',
  initialState: State,
  reducers: {
    // 로그아웃
    logout(state) {
      alert('로그아웃 되었습니다.');
      console.log('로그아웃');

      localStorage.clear();
      state.user = { ...State.user, type: USER_TYPE.USER_LOGOUT };
    },
  },
  extraReducers: (builder) => {
    // 회원가입
    builder.addCase(register.pending, (state) => {
      state.status = 'loading';
    });
    builder.addCase(register.fulfilled, (state, { type, payload }) => {
      state.status = 'success';
    });
    builder.addCase(register.rejected, (state, { type, payload }) => {
      state.status = 'failed';
    });

    // 로그인
    builder.addCase(login.pending, (state) => {
      state.status = 'loading';
    });
    builder.addCase(login.fulfilled, (state, { type, payload }) => {
      state.status = 'success';
    });
    builder.addCase(login.rejected, (state, { type, payload }) => {
      state.status = 'failed';
    });

    // 내 정보
    builder.addCase(infoMy.pending, (state) => {
      state.status = 'loading';
    });
    builder.addCase(infoMy.fulfilled, (state, { type, payload }) => {
      state.status = 'success';
      // @ts-ignore
      const res = payload.payload;

      // @ts-ignore
      state.user = { type: USER_TYPE.USER_LOGIN, ...res };
    });
    builder.addCase(infoMy.rejected, (state, { type, payload }) => {
      state.status = 'failed';
    });

    // 토큰 체크
    builder.addCase(checkToken.pending, (state) => {
      state.status = 'loading';
      // state.auth = {};
    });
    builder.addCase(checkToken.fulfilled, (state, { type, payload }) => {
      state.status = 'success';
      const data = payload as { type: string; success: boolean; message: string };
      state.auth.type = type;
      state.auth.payload = data;
    });
    builder.addCase(checkToken.rejected, (state, { type, payload }) => {
      const data = payload as AuthType;
      state.status = 'failed';
      state.auth.type = type;
      state.auth.payload = data.payload;
    });
  },
});

export const { logout } = user.actions;
export const userStatus = (state: ReducerType) => state.users.user;
export const auth = (state: ReducerType) => state.users.auth.payload;
export default user.reducer;
