import axios from "axios";
import { isProd } from "@Util";
export const API_URL = isProd ? "/api" : process.env.NEXT_PUBLIC_API_URL;

const axiosHeaders = {
	"Access-Control-Allow-Credentials": true,
	"Content-Type": "application/json",
	"Access-Control-Allow-Origin": "*",
};

export async function getDataFromAPI(route = null, headers, ...params) {
	if (route === null) return null;
	try {
		const response = await axios.get(`${API_URL}.${route}`, {
			headers: {
				...axiosHeaders,
				...headers,
			},
			...params,
		});
		console.log(response);
		return response.data;
	} catch (err) {
		console.error(err);
	}
}
export async function postDataFromAPI(route = null, headers, ...params) {
	if (route === null) return null;
	try {
		const response = await axios.post(`${API_URL}.${route}`, {
			headers: {
				...axiosHeaders,
				...headers,
			},
			...params,
		});
		console.log(response);
		return response;
	} catch (err) {
		throw err;
	}
}
