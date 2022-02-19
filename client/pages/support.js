import Head from "next/head";
import { TEAM_MEMBERS } from "@CONSTANTS";

// Hooks
import { useState } from "react";
import { useTheme } from "@Theme";

// Components
import { Loading } from "@Components";
import {
	Stack,
	Button,
	Heading,
	Text,
	Input,
	Textarea,
	Tooltip,
	Divider,
	Box,
	useColorModeValue,
	HStack,
	Link,
} from "@chakra-ui/react";
import { FiMail, FiFacebook } from "react-icons/fi";
import { MdOutlineMarkEmailRead } from "react-icons/md";
export default function Support() {
	const [subject, setSubject] = useState("");
	const [content, setContent] = useState("");
	const bgColor = useColorModeValue("white", "gray.800");

	return (
		<Stack spacing={6} w='full' align='center' justify='center' h='full'>
			<Tooltip
				hasArrow
				placement='top'
				label='Send mail direct from mail provider'>
				<Link
					isExternal
					_hover={{ textDecoration: "none" }}
					href='mailto:ZSDP@sharkia1.moe.edu.eg'>
					<Button variant={"outline"} leftIcon={<FiMail size='1.4em' />}>
						ZSDP@sharkia1.moe.edu.eg
					</Button>
				</Link>
			</Tooltip>
			<Stack align={"center"} w={"96"}>
				<Stack w='full' spacing={1}>
					<Heading size='xs'>Subject</Heading>
					<Input
						variant={"filled"}
						value={subject}
						onChange={(e) => setSubject(e.target.value)}
					/>
				</Stack>
				<Stack w='full' spacing={1}>
					<Heading size='xs'>Content</Heading>

					<Textarea
						variant={"filled"}
						value={content}
						onChange={(e) => setContent(e.target.value)}
					/>
				</Stack>
				<Button maxW={28} rightIcon={<MdOutlineMarkEmailRead size='1.4em' />}>
					Submit
				</Button>
			</Stack>
			<Stack w='full' align={"center"}>
				<Stack align={"center"} justify='center' position={"relative"} w='full'>
					<Divider />
					<Heading
						px={3}
						bgColor={bgColor}
						top={-3}
						fontSize='x-small'
						position={"absolute"}>
						OR
					</Heading>
				</Stack>
				<Stack align={"center"}>
					<Heading size='xs'>Contect with us by</Heading>
					<Tooltip hasArrow label='Our Facebook Page'>
						<Link
							isExternal
							_hover={{ textDecoration: "none" }}
							href='https://www.facebook.com/ZSDP.Official'>
							<Button
								colorScheme='facebook'
								leftIcon={<FiFacebook size='1.4em' />}
								size='xs'>
								ZSDP Official
							</Button>
						</Link>
					</Tooltip>
				</Stack>
			</Stack>
		</Stack>
	);
}
