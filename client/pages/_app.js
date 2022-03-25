import { PageLayout } from "@Layout/PageLayout";
import { breakpoints } from "@Theme";
import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { StepsStyleConfig as Steps } from "chakra-ui-steps";

const theme = extendTheme({
	components: {
		Steps,
	},
	breakpoints,
});

import "../styles/globals.css";

function App({ Component, pageProps }) {
	return (
		<ChakraProvider theme={theme}>
			<PageLayout>
				<Component {...pageProps} />
			</PageLayout>
		</ChakraProvider>
	);
}

export default App;
