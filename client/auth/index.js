import { useContext, createContext, useState, useEffect } from "react";
import axios from "axios";
import cookie from "js-cookie";
import { useToast } from "@chakra-ui/react";
import { request } from "@API";

const AuthContext = createContext();

export default function AuthProvider({ children }) {
	const [username, setUsername] = useState(null);
	const [sessionID, setSessionID] = useState(null);
	const [isSignedIn, setIsSignedIn] = useState(false);
	const toast = useToast();

	useEffect(() => {
		if (
			typeof cookie.get("username") !== "undefined" &&
			typeof cookie.get("session_id") !== "undefined"
		) {
			setIsSignedIn(true);
			setUsername(cookie.get("username"));
			setSessionID(cookie.get("session_id"));
		}
	});

	async function signin({ user, pass }) {
		const response = await request(
			"post",
			"login"
		)({
			user,
			pass,
		});
		const data = response.data;
		if (data?.status === "failed") {
			toast({
				position: "top",
				title: "Login was failed.",
				description: data?.msg,
				status: "error",
				duration: 9000,
				isClosable: true,
			});
			return;
		}
		setUsername(data?.first_name);
		setSessionID(data?.session_id);
		cookie.set("username", data?.first_name);
		cookie.set("session_id", data?.session_id);
	}

	function signout() {
		cookie.remove("username");
		cookie.remove("session_id");

		setUsername(null);
		setSessionID(null);
		setIsSignedIn(false);
	}

	const value = {
		isSignedIn,
		signin,
		signout,
		username,
	};
	return (
		<AuthContext.Provider value={value}>{children}</AuthContext.Provider>
	);
}

export function useAuth() {
	return useContext(AuthContext);
}
