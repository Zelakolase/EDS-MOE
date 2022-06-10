import Head from "next/head";
import { TEAM_MEMBERS } from "@CONSTANTS";
import { request } from "@API";
// Hooks
import { useEffect, useState } from "react";
import { useTheme } from "@Theme";

// Components
import { Loading } from "@Components";
import {
	Stack,
	Button,
	Heading,
	Text,
	Link,
	UnorderedList,
	ListItem,
} from "@chakra-ui/react";

export default function About() {
	const [count, setCount] = useState({
		queries: null,
		documents: null,
	});

	useEffect(async () => {
		try {
			const response = await request("get", "about")();
			const data = response.data;
			console.debug(data);
			setCount({
				queries: response.data.query_num,
				queries: response.data.document_num,
			});
		} catch (e) {
			toast({
				position: "top",
				title: "Login was failed.",
				description: err.toString(),
				status: "error",
				duration: 9000,
				isClosable: true,
			});
		}
	}, []);
	return (
		<Stack spacing={24} align="center" justify="center" h="full">
			<Stack>
				<Heading textAlign={"center"}>
					Electronic Document System
				</Heading>
				<Text textAlign={"center"}>
					This system was made by{" "}
					<Link
						isExternal
						href="https://www.facebook.com/ZSDP.Official"
						textDecoration={"underline"}>
						ZSDP
					</Link>{" "}
					Team. We currently have{" "}
					<span>
						{count["documents"] !== null ? documentsCount : "many"}
					</span>{" "}
					documents on our system. And{" "}
					<span>
						{count["queries"] !== null ? count["queries"] : "many"}
					</span>{" "}
					queries processed.
				</Text>
			</Stack>
			<Stack alignSelf={"start"}>
				<Heading size="md" textTransform={"capitalize"}>
					team members
				</Heading>
				<UnorderedList pl={8}>
					{TEAM_MEMBERS.map((member, index) => (
						<ListItem font key={index} textTransform={"capitalize"}>
							<Heading size="xs">{member}</Heading>
						</ListItem>
					))}
				</UnorderedList>
			</Stack>
		</Stack>
	);
}
