import { useState, useEffect } from "react";
import { useAuth } from "@Auth";
import { useRouter } from "next/router";

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
	Form,
	useToast,
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
	const { signin, isAuth, username } = useAuth();

	const toast = useToast();
	const router = useRouter();

	const [form, setForm] = useState({ user: "", pass: "" });
	const [isLoading, setIsLoading] = useState(false);

	const [isPassFieldVisiable, setIsPassFieldVisiable] = useState(false);
	const [passwordPlaceholder, _] = useState(generatingRandomPassword());

	async function signInHandler() {
		setIsLoading(true);

		try {
			await signin(form);
			toast({
				position: "top",
				description: `Welcome back!`,
				status: "success",
				duration: 9000,
				isClosable: true,
			});
		} catch (err) {
			toast({
				position: "top",
				title: "Login was failed.",
				description: err.toString(),
				status: "error",
				duration: 9000,
				isClosable: true,
			});
		}
		setIsLoading(false);
	}

	useEffect(() => {
		if (isAuth) router.replace("/submit");
	});

	return (
		<FormControl
			as={Stack}
			w="full"
			maxW={"96"}
			spacing={6}
			padding={4}
			h="full"
			align="center"
			justify="center">
			<Logo />
			<Stack w="full" spacing={1}>
				<HStack align="end">
					<AiOutlineUser size="1.3em" />
					<FormLabel htmlFor="username" fontSize={13}>
						Username
					</FormLabel>
				</HStack>
				<Input
					placeholder="username"
					id="username"
					variant={"filled"}
					value={form.user}
					onChange={e => setForm({ ...form, user: e.target.value })}
				/>
			</Stack>
			<Stack w="full" spacing={1}>
				<HStack align="end">
					<BsKey size="1.3em" />
					<FormLabel htmlFor="password" fontSize={13}>
						Password
					</FormLabel>
				</HStack>
				<Box
					position={"relative"}
					w="full"
					display={"flex"}
					alignContent="center">
					<Input
						placeholder={passwordPlaceholder}
						variant={"filled"}
						id="password"
						type={isPassFieldVisiable ? "text" : "password"}
						value={form.pass}
						onChange={e =>
							setForm({ ...form, pass: e.target.value })
						}
					/>

					<IconButton
						position={"absolute"}
						right={1}
						variant="ghost"
						alignSelf={"center"}
						onClick={() =>
							setIsPassFieldVisiable(!isPassFieldVisiable)
						}
						size="sm"
						icon={
							isPassFieldVisiable ? (
								<AiOutlineEye icon="1.4em" />
							) : (
								<AiOutlineEyeInvisible icon="1.4em" />
							)
						}
					/>
				</Box>
			</Stack>
			<Button
				onClick={signInHandler}
				rightIcon={<BiLogIn size="1.4em" />}
				isLoading={isLoading}>
				Login
			</Button>
		</FormControl>
	);
}
