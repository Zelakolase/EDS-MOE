import axios from "axios";
import { isProd } from "@Util";
export const API_URL = isProd ? "/api" : process.env.NEXT_PUBLIC_API_URL;

const DefaultHeaders = {
	"Access-Control-Allow-Credentials": true,
	"Content-Type": "application/json",
	"Access-Control-Allow-Origin": "*",
};

export function request(method, route) {
	return async (body, headers) => {
		// console.log(body, headers);
		return await axios[method](`${API_URL}.${route}`, body, {
			headers: { ...DefaultHeaders, ...headers },
		});
	};
}
