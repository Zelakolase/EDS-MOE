import { PageLayout } from "@Layout/PageLayout";
import { breakpoints } from "@Theme";
import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { StepsStyleConfig as Steps } from "chakra-ui-steps";
import AuthProvider from "@Auth";
import "../styles/globals.css";

const theme = extendTheme({
	components: {
		Steps,
	},
	breakpoints,
});

function App({ Component, pageProps }) {
	return (
		<ChakraProvider theme={theme}>
			<AuthProvider>
				<PageLayout>
					<Component {...pageProps} />
				</PageLayout>
			</AuthProvider>
		</ChakraProvider>
	);
}

export default App;
