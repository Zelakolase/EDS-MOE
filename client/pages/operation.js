import { useState } from "react";
import { useRouter } from "next/router";
import { useWindowSize } from "rooks";
import { MINI_WIDTH_SCREEN } from "@Theme";
import { Logo, FileInfo } from "@Components";
import {
	Stack,
	Button,
	Input,
	Heading,
	HStack,
	Box,
	Text,
	IconButton,
	Tooltip,
	Divider,
	useBreakpointValue,
} from "@chakra-ui/react";
import { Step, Steps, useSteps } from "chakra-ui-steps";
import { AiOutlineArrowRight, AiOutlineUpload } from "react-icons/ai";
import { RiFolderInfoLine } from "react-icons/ri";
export default function Login() {
	const { innerWidth } = useWindowSize();
	const router = useRouter();
	const { nextStep, prevStep, setStep, reset, activeStep } = useSteps({
		initialStep: 0,
	});

	let steps = [
		{
			label: "Generate Document",
			component: <GenerateDocument />,
		},
		{
			label: "Upload",
			component: <Upload />,
		},
		{
			label: "Done",
			component: <Done />,
		},
	];

	const isLastStep = activeStep === steps.length - 1;
	const isFirstStep = activeStep === 0;

	return (
		<Stack
			w='full'
			h='full'
			spacing={6}
			padding={4}
			justify='space-around'
			align='center'>
			<Heading>Welcome, Username</Heading>
			<Stack w='full' h='full'>
				<Steps activeStep={activeStep} labelOrientation={"vertical"}>
					{steps.map(({ label, component }) => (
						<Step
							alignSelf={innerWidth > MINI_WIDTH_SCREEN ? "center" : "auto"}
							label={label}>
							<Stack w='full' h='full' align='center' justify='center'>
								{component}
							</Stack>
						</Step>
					))}
				</Steps>
			</Stack>
			<HStack alignSelf={"end"}>
				{!isFirstStep && (
					<Button onClick={prevStep} variant={"ghost"} size='sm'>
						Previous
					</Button>
				)}
				<Button
					size='sm'
					onClick={() => (isLastStep ? router.push("/") : nextStep())}>
					{activeStep === steps.length - 1 ? "Finish" : "Next"}
				</Button>
			</HStack>
		</Stack>
	);
}

function GenerateDocument() {
	const [info, setInfo] = useState({ documentName: "", date: "", writer: "" });
	return (
		<Stack spacing={6} my={6} maxW={80}>
			<Stack w='full' spacing={1}>
				<HStack align='end'>
					<Heading fontSize={13}>Document Name</Heading>
				</HStack>
				<Input
					variant={"filled"}
					value={info.documentName}
					onChange={(e) => setInfo({ ...info, documentName: e.target.value })}
				/>
			</Stack>
			<Stack w='full' spacing={1}>
				<HStack align='end'>
					<Heading fontSize={13}>Date of publication</Heading>
				</HStack>
				<Box
					position={"relative"}
					w='full'
					display={"flex"}
					alignContent='center'>
					<Input
						variant={"filled"}
						value={info.date}
						onChange={(e) => setInfo({ ...info, date: e.target.value })}
					/>
				</Box>
			</Stack>
			<Stack>
				<HStack align='end'>
					<Heading fontSize={13}>Writer</Heading>
				</HStack>
				<Input
					variant={"filled"}
					value={info.writer}
					onChange={(e) => setInfo({ ...info, writer: e.target.value })}
				/>
			</Stack>
		</Stack>
	);
}

function Upload() {
	const buttonSize = useBreakpointValue({
		xs: "sm",
		md: "lg",
	});
	const [file, setFile] = useState();
	const [verifyCode, setVerifyCode] = useState("");

	return (
		<Stack w='full' h='full' spacing={2} justify='center' align={"center"}>
			<Text>
				Please, Insert Verification code inside the document, then upload it
				below.
			</Text>
			<Stack>
				<Tooltip
					hasArrow
					placement='top'
					label='Import the document which you need to verify'>
					<Box position='relative' w={"max-content"}>
						<Button
							size={buttonSize}
							rightIcon={<AiOutlineUpload size='1.4em' />}>
							Upload File
						</Button>
						<Input
							w='full'
							type={"file"}
							left={0}
							onChange={(e) => setFile(e.target.files[0])}
							top={0}
							cursor={"pointer"}
							position={"absolute"}
							opacity={0}
							w='full'
							h='full'
						/>
					</Box>
				</Tooltip>
				<HStack justify={"end"} align='end' w='full'>
					{file && <FileInfo file={file} />}
				</HStack>
			</Stack>
		</Stack>
	);
}

function Done() {
	return (
		<Stack align={"center"}>
			<Text fontSize={18}>The document is uploaded successfully !</Text>
			<Divider />
			<Stack>
				<Stack spacing={0}>
					<Heading fontSize={12}>Public code</Heading>
					<Text>[Pubic code placeholder]</Text>
				</Stack>
				<Stack spacing={0}>
					<Heading fontSize={12}>Verification code</Heading>
					<Text>[Verification code placeholder]</Text>
				</Stack>
			</Stack>
		</Stack>
	);
}
