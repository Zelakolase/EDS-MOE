import axios from "axios";
import { isProd } from "@Util";

export const API_URL = isProd ? "/api" : process.env.NEXT_PUBLIC_API_URL;

const DefaultHeaders = {
	"Access-Control-Allow-Credentials": true,
	"Content-Type": "application/json",
	"Access-Control-Allow-Origin": "*",
	"Access-Control-Allow-Methods": "*",
	"Access-Control-Allow-Headers": "*",
};

export function request(method, route) {
	return async (data, headers, props = {}) => {
		let config = {
			method,
			url: `${API_URL}.${route}`,
			headers: { ...DefaultHeaders, ...headers },
			data,
		};

		return await axios(config);
	};
}
