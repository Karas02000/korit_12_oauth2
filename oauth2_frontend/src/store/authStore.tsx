import { useState, createContext, useContext } from "react";
import type { ReactNode } from "react";
import type { UserInfo, AuthContextType } from "../types/auth";

const TOKEN_KEY = 'jwt_token';
const USER_KEY = 'user_info';

const AuthContext = createContext<AuthContextType | null> (null);

interface AuthProviderProps {
    children: ReactNode;
}

export function AuthProvider({ children } : AuthProviderProps) {

    const [ user, setUser ] = useState<UserInfo | null>(() => {
        const savedUser = localStorage.getItem(USER_KEY);
        return savedUser ? (JSON.parse(savedUser) as UserInfo) : null;
    });

    const login = (token: string, userInfo: UserInfo): void => {
        localStorage.setItem(TOKEN_KEY, token);
        localStorage.setItem(USER_KEY, JSON.stringify(userInfo));
        setUser(userInfo);
    };

    const logout = () => {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        setUser(null);
    };

    const getToken = () : string | null => localStorage.getItem(TOKEN_KEY);
    const isLoggedIn : boolean = !!user;

    return(
        <AuthContext.Provider value={{user, login, logout, getToken, isLoggedIn}}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() : AuthContextType {
    const context = useContext(AuthContext);
    if(!context) {
        throw new Error("useAuth는 AuthProvider 내부에서만 사용할 수 있습니다.")
    }

    return context;
}