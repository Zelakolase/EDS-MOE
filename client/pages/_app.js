import { PageLayout } from "@Layout/PageLayout";
import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { StepsStyleConfig as Steps } from "chakra-ui-steps";

const theme = extendTheme({
	components: {
		Steps,
	},
});

import "../styles/globals.css";

function MyApp({ Component, pageProps }) {
	return (
		<ChakraProvider theme={theme}>
			<PageLayout>
				<Component {...pageProps} />
			</PageLayout>
		</ChakraProvider>
	);
}

export default MyApp;
