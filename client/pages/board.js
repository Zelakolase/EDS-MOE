import { useEffect, useState } from "react";
import { useRouter } from "next/router";

import { request } from "@API";
import { TABLE } from "@API/endpoints";
import { useAuth } from "@Auth";

import {
	Stack,
	Spinner,
	Box,
	Heading,
	Text,
	Link,
	Table,
	Thead,
	Tbody,
	Tfoot,
	Tr,
	Th,
	Td,
	TableCaption,
	TableContainer,
	useToast,
} from "@chakra-ui/react";
import { motion } from "framer-motion";

export default function Board() {
	const { isAuth, username, sessionID } = useAuth();
	const router = useRouter();
	const toast = useToast();

	const [data, setData] = useState(null);
	const [loading, setLoading] = useState(false);

	useEffect(async () => {
		setLoading(true);
		try {
			const response = await request(
				"get",
				TABLE
			)({ session_id: sessionID });

			setData(Object.values(response.data));
		} catch (err) {
			console.error(err);
			toast({
				title: "Get data failed.",
				description: err.toString(),
				status: "error",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
		}
		setLoading(false);
	}, []);

	useEffect(() => {
		if (!isAuth) router.replace("/auth/login");
	});

	if (loading)
		return (
			<Stack h="full" align={"center"} justify="center">
				<Spinner size="lg" />
				<motion.div
					transition={{
						duration: 0.5,
						repeat: Infinity,
						repeatType: "mirror",
					}}
					initial={{ opacity: 0.8 }}
					animate={{ opacity: 1 }}>
					<Box>
						<Heading size={"xs"}>
							Please wait few seconds...
						</Heading>
					</Box>
				</motion.div>
			</Stack>
		);

	if (data === null)
		return (
			<Stack h="full" align={"center"}>
				<Heading size={"lg"}>Cannot found data</Heading>
				<Text>
					Please, contact with{" "}
					<Link
						color="teal.500"
						onClick={() => router.push("/support")}>
						support
					</Link>{" "}
					solve this issue.
				</Text>
			</Stack>
		);
	return (
		<Stack>
			<TableContainer>
				<Table variant="simple">
					<TableCaption>Table of verifiers' codes</TableCaption>
					<Thead>
						<Tr>
							{Object.keys(data[0]).map(key => (
								<Th>{key}</Th>
							))}
						</Tr>
					</Thead>
					<Tbody>
						{data.map(col => (
							<Tr>
								{Object.values(col).map(val => (
									<Td>{val}</Td>
								))}
							</Tr>
						))}
					</Tbody>
				</Table>
			</TableContainer>
		</Stack>
	);
}
