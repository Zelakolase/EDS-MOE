import Head from "next/head";
import { useRouter } from "next/router";

import { PageLayout } from "@Layout/PageLayout";
import { breakpoints } from "@Theme";
import AuthProvider from "@Auth";

import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { StepsStyleConfig as Steps } from "chakra-ui-steps";

import "../styles/globals.css";
import { useEffect, useState } from "react";

const theme = extendTheme({
	components: {
		Steps,
	},
	breakpoints,
});

function App({ Component, pageProps }) {
	let router = useRouter();
	const [title, setTitle] = useState("");
	// console.log({ ...router });

	useEffect(() => {
		setTitle(() => {
			switch (router.pathname) {
				case "/":
					return "| Home";
				case "/404":
					return "| Not found";
				default:
					return `| ${router?.pathname
						?.split("/")
						?.at(-1)
						?.split("")
						.map((c, index) => (index === 0 ? c.toUpperCase() : c))
						.join("")}`;
			}
		});
	}, [router]);

	return (
		<>
			<Head>
				<title>EDS {title}</title>
				<meta
					content={`Electronic Document System`}
					propery="og:title"
				/>
			</Head>
			<ChakraProvider theme={theme}>
				<AuthProvider>
					<PageLayout>
						<Component {...pageProps} />
					</PageLayout>
				</AuthProvider>
			</ChakraProvider>
		</>
	);
}

export default App;
