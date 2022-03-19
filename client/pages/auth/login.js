import { useState } from "react";

import { Logo } from "@Components";
import {
	Stack,
	Button,
	Input,
	Heading,
	HStack,
	Box,
	IconButton,
	Tooltip,
} from "@chakra-ui/react";

import {
	AiOutlineUser,
	AiOutlineEye,
	AiOutlineEyeInvisible,
} from "react-icons/ai";
import { BsKey } from "react-icons/bs";
import { BiLogIn } from "react-icons/bi";
export default function Login() {
	const [info, setInfo] = useState({ username: "", password: "" });
	const [isPassFieldVisiable, setIsPassFieldVisiable] = useState(false);
	return (
		<Stack w='full' maxW={"96"} spacing={6} padding={4} align='center' h='full'>
			<Logo />
			<Stack w='full' spacing={1}>
				<HStack align='end'>
					<AiOutlineUser size='1.3em' />
					<Heading fontSize={13}>Username</Heading>
				</HStack>
				<Input
					variant={"filled"}
					value={info.username}
					onChange={(e) => setInfo({ ...info, username: e.target.value })}
				/>
			</Stack>
			<Stack w='full' spacing={1}>
				<HStack align='end'>
					<BsKey size='1.3em' />
					<Heading fontSize={13}>Password</Heading>
				</HStack>
				<Box
					position={"relative"}
					w='full'
					display={"flex"}
					alignContent='center'>
					<Input
						variant={"filled"}
						type={isPassFieldVisiable ? "text" : "password"}
						value={info.password}
						onChange={(e) => setInfo({ ...info, password: e.target.value })}
					/>

					<IconButton
						position={"absolute"}
						right={1}
						variant='ghost'
						alignSelf={"center"}
						onClick={() => setIsPassFieldVisiable(!isPassFieldVisiable)}
						size='sm'
						icon={
							isPassFieldVisiable ? (
								<AiOutlineEye icon='1.4em' />
							) : (
								<AiOutlineEyeInvisible icon='1.4em' />
							)
						}
					/>
				</Box>
			</Stack>
			<Button maxW={28} rightIcon={<BiLogIn size='1.4em' />}>
				Login
			</Button>
		</Stack>
	);
}
