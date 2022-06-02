import {
	Stack,
	Heading,
	Text,
	Button,
	HStack,
	Divider,
} from "@chakra-ui/react";
import { useRouter } from "next/router";

export default function PageNotFound() {
	const router = useRouter();
	return (
		<Stack h="full" align="center" justify={"center"} spacing={7}>
			<HStack spacing={3}>
				<Heading size={"4xl"}>404</Heading>
				<Divider orientation="vertical" />
				<Text fontSize="2xl" textTransform={"uppercase"}>
					not found
				</Text>
			</HStack>
			<Button onClick={() => router.push("/")}>Back Home</Button>
		</Stack>
	);
}
