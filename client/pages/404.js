import { Stack, Heading, Text, Button } from "@chakra-ui/react";
import { useRouter } from "next/router";

export default function PageNotFound() {
	const router = useRouter();
	return (
		<Stack h='full' align='center' justify={"center"}>
			<Heading>404</Heading>
			<Text>Page isn't found</Text>
			<Button onClick={() => router.push("/")}>Back Home</Button>
		</Stack>
	);
}
