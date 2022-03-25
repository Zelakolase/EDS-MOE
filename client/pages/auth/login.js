import { useState } from "react";

import { Logo } from "@Components";
import {
	Stack,
	Button,
	Input,
	HStack,
	Box,
	IconButton,
	Tooltip,
	FormControl,
	FormLabel,
	FormErrorMessage,
	FormHelperText,
} from "@chakra-ui/react";

import {
	AiOutlineUser,
	AiOutlineEye,
	AiOutlineEyeInvisible,
} from "react-icons/ai";
import { BsKey } from "react-icons/bs";
import { BiLogIn } from "react-icons/bi";

function generatingRandomPassword() {
	let chars =
		"0123456789abcdefghijklmnopqrstuvwxyz!@#$%^&*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	let password = "";
	for (let i = 0; i < Math.floor(Math.random() * 16) + 8; i++) {
		password += chars[Math.floor(Math.random() * (chars.length - 1))];
	}
	return password;
}

export default function Login() {
	const [info, setInfo] = useState({ username: "", password: "" });
	const [isPassFieldVisiable, setIsPassFieldVisiable] = useState(false);
	const [passwordPlaceholder, _] = useState(generatingRandomPassword());
	return (
		<FormControl
			as={Stack}
			w='full'
			maxW={"96"}
			spacing={6}
			padding={4}
			align='center'
			h='full'
			justify={"center"}>
			<Logo />
			<Stack w='full' spacing={1}>
				<HStack align='end'>
					<AiOutlineUser size='1.3em' />
					<FormLabel for='username' fontSize={13}>
						Username
					</FormLabel>
				</HStack>
				<Input
					placeholder='username'
					id='username'
					variant={"filled"}
					value={info.username}
					onChange={(e) => setInfo({ ...info, username: e.target.value })}
				/>
			</Stack>
			<Stack w='full' spacing={1}>
				<HStack align='end'>
					<BsKey size='1.3em' />
					<FormLabel htmlfor='password' fontSize={13}>
						Password
					</FormLabel>
				</HStack>
				<Box
					position={"relative"}
					w='full'
					display={"flex"}
					alignContent='center'>
					<Input
						placeholder={passwordPlaceholder}
						variant={"filled"}
						id='password'
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
			<Button rightIcon={<BiLogIn size='1.4em' />}>Login</Button>
		</FormControl>
	);
}
