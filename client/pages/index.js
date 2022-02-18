import Head from "next/head";
import { Loading } from "@Components";

import { Stack, Button, Heading } from "@chakra-ui/react";

import { useTheme } from "@Theme";
export default function Home() {
	const { bg, bgHover, color } = useTheme();
	return (
		<Stack align='center' justify='center' h='full'>
			<Stack spacing='-0.5' align='center'>
				<Heading size='sm'>Welcome to</Heading>
				<Heading textAlign={"center"}>Electronic Document System</Heading>
			</Stack>
			<Button maxW={100}>Templete</Button>
		</Stack>
	);
}
