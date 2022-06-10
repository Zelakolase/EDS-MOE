import Head from "next/head";
import { useRouter } from "next/router";

import { PageLayout } from "@Layout/PageLayout";
import { breakpoints } from "@Theme";
import AuthProvider from "@Auth";

import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { StepsStyleConfig as Steps } from "chakra-ui-steps";

import "../styles/globals.css";

const theme = extendTheme({
	components: {
		Steps,
	},
	breakpoints,
});

function App({ Component, pageProps }) {
	let router = useRouter();
	console.log({ ...router });
	let title = `EDS | ${(() => {
		switch (router.pathname) {
			case "/":
				return "Home";
			case "/404":
				return "Not found";
			default:
				return router.pathname
					.split("/")
					.at(-1)
					.split("")
					.map((c, index) => (index === 0 ? c.toUpperCase() : c))
					.join("");
		}
	})()} `;

	return (
		<>
			<Head>
				<title>{title}</title>
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
