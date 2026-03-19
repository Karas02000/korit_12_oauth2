export interface SignupRequest {
    email: string;
    password: string;
    name: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    email: string;
    name: string;
    role: string;
}

export interface UserInfo {
    email: string;
    name: string;
    role: string;
}

export type SignupFormErrors = Partial<SignupRequest>;
export type LoginFormErrors = Partial<LoginRequest>;

export interface AuthContextType {
    user: UserInfo | null;
    login: (token: string, userInfo: UserInfo) => void;
    logout: () => void;
    getToken: () => string | null;
    isLoggedIn: boolean;
}