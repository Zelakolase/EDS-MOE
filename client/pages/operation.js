import { useState, useEffect, useContext, createContext, useRef } from "react";
import { useRouter } from "next/router";

import { useAuth } from "@Auth";
import { request } from "@API";
import { useContextState } from "@Hooks";

import { useWindowSize } from "rooks";
import { MINI_WIDTH_SCREEN } from "@Theme";
import { Logo, FileInfo } from "@Components";
import {
	Stack,
	Button,
	Input,
	InputGroup,
	InputRightAddon,
	Heading,
	HStack,
	Box,
	Text,
	IconButton,
	Tooltip,
	Divider,
	useBreakpointValue,
	useToast,
} from "@chakra-ui/react";
import { Step, Steps, useSteps } from "chakra-ui-steps";

import { AiOutlineArrowRight, AiOutlineUpload } from "react-icons/ai";
import { RiFolderInfoLine } from "react-icons/ri";
import { MdDateRange } from "react-icons/md";
const OperaionContext = createContext();

const useOperation = () => useContext(OperaionContext);

export default function Operation() {
	const { isSignedIn, username, sessionID } = useAuth();

	const router = useRouter();
	const toast = useToast();

	const { innerWidth } = useWindowSize();
	const { nextStep, prevStep, setStep, reset, activeStep } = useSteps({
		initialStep: 0,
	});

	// States
	const [isNextButtonDisabled, setIsNextButtonDisabled] = useState(false);
	const [isNextButtonLoading, setIsNextButtonLoading] = useState(false);
	const [generatedDocResult, setGenerateDocResult] = useState(null);
	const [uploadDocResult, setuploadDocResult] = useState(null);

	const [state, stateSetter] = useContextState({
		generateDocument: {
			document_name: "",
			date: "",
			writer: "",
		},
		upload: {
			file: null,
		},
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

	async function generateDoc() {
		setIsNextButtonLoading(true);
		try {
			// Destructing
			const { generateDocument } = state;
			const { documentName: doc_name, date, writer } = generateDocument;

			const response = await request(
				"post",
				"generate"
			)({ doc_name, date, writer, session_id: sessionID });
			if (response?.data?.status === "failed") throw response?.data?.msg;

			setGenerateDocResult(response?.data);
			nextStep();
		} catch (err) {
			toast({
				title: "Generate failed.",
				description: err.toString(),
				status: "error",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
		}
		setIsNextButtonLoading(false);
	}
	async function uploadDoc() {
		setIsNextButtonLoading(true);
		try {
			const response = await request(
				"post",
				"genrate"
			)(...{ session_id: sessionID });
			if (response?.data?.status === "failed") throw response?.data?.msg;

			setGeneratedDocResilt(response?.data);
			nextStep();
		} catch (err) {
			toast({
				title: "Generate failed.",
				description: err.toString(),
				status: "error",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
		}
		setIsNextButtonLoading(false);
	}
	useEffect(() => {
		if (!isSignedIn) router.replace("/auth/login");
	});

	return (
		<OperaionContext.Provider
			value={{
				generateDoc,
				generatedDocResult,
				uploadDoc,
				uploadDocResult,
				stateSetter,
				...state.generateDocument,
				...state.upload,
				setIsNextButtonDisabled,
			}}>
			<Stack
				w="full"
				h="full"
				spacing={6}
				padding={4}
				justify="space-around"
				align="center">
				<Heading>Welcome, {username}</Heading>
				<Stack w="full" h="full">
					<Steps
						activeStep={activeStep}
						labelOrientation={"vertical"}>
						{steps.map(({ label, component }, index) => (
							<Step
								key={index}
								alignSelf={
									innerWidth > MINI_WIDTH_SCREEN
										? "center"
										: "auto"
								}
								label={label}>
								<Stack
									w="full"
									h="full"
									align="center"
									justify="center">
									{component}
								</Stack>
							</Step>
						))}
					</Steps>
				</Stack>
				<HStack alignSelf={"end"}>
					<Button
						isDisabled={isNextButtonDisabled}
						isLoading={isNextButtonLoading}
						size="sm"
						onClick={async () =>
							// isLastStep ? router.push("/") : nextStep()
							{
								switch (activeStep) {
									case 0:
										await generateDoc();
										break;

									case 1:
										await uploadDoc();
										break;
									case isLastStep:
										router.push("/");
										break;
									default:
										break;
								}
							}
						}>
						{activeStep === steps.length - 1 ? "Finish" : "Next"}
					</Button>
				</HStack>
			</Stack>
		</OperaionContext.Provider>
	);
}

function GenerateDocument() {
	const datePickerRef = useRef();

	const {
		document_name,
		writer,
		date,
		stateSetter,
		setIsNextButtonDisabled,
	} = useOperation();

	useEffect(() => {
		if ([document_name, writer, date].filter(e => !e).length > 0)
			setIsNextButtonDisabled(true);
		else setIsNextButtonDisabled(false);
	}, [document_name, writer, date]);

	return (
		<Stack spacing={6} my={6} maxW={80}>
			<Stack w="full" spacing={1}>
				<HStack align="end">
					<Heading fontSize={13}>Document Name</Heading>
				</HStack>
				<Input
					variant={"filled"}
					value={document_name}
					onChange={e =>
						stateSetter(
							"generateDocument.document_name",
							e.target.value
						)
					}
				/>
			</Stack>
			<Stack w="full" spacing={1}>
				<HStack align="end">
					<Heading fontSize={13}>Date of publication</Heading>
				</HStack>
				<Box
					position={"relative"}
					w="full"
					display={"flex"}
					alignContent="center">
					{/* <InputGroup size="md"> */}
					<Input
						variant={"filled"}
						value={date}
						ref={datePickerRef}
						type="date"
						onChange={e =>
							stateSetter("generateDocument.date", e.target.value)
						}
					/>
				</Box>
			</Stack>
			<Stack>
				<HStack align="end">
					<Heading fontSize={13}>Writer</Heading>
				</HStack>
				<Input
					variant={"filled"}
					value={writer}
					onChange={e =>
						stateSetter("generateDocument.writer", e.target.value)
					}
				/>
			</Stack>
		</Stack>
	);
}

function Upload() {
	const { stateSetter, file, setIsNextButtonDisabled } = useOperation();

	const buttonSize = useBreakpointValue({
		xs: "sm",
		md: "lg",
	});

	useEffect(() => {
		if (file === null) setIsNextButtonDisabled(true);
		else setIsNextButtonDisabled(false);
	}, [file]);

	return (
		<Stack w="full" h="full" spacing={2} justify="center" align={"center"}>
			<Text>
				Please, Insert Verification code inside the document, then
				upload it below.
			</Text>
			<HStack>
				<Tooltip
					hasArrow
					placement="top"
					label="Import the document which you need to verify">
					<Box position="relative" w={"max-content"}>
						<Button
							size={buttonSize}
							rightIcon={<AiOutlineUpload size="1.4em" />}>
							Upload File
						</Button>
						<Input
							w="full"
							type={"file"}
							accept=".pdf"
							left={0}
							// value={file}
							onChange={e => {
								let __file__ = e.target.files[0];
								console.log(__file__);
								if (typeof __file__ !== "undefined") {
									stateSetter("upload.file", __file__);
								}
							}}
							top={0}
							cursor={"pointer"}
							position={"absolute"}
							opacity={0}
							w="full"
							h="full"
						/>
					</Box>
				</Tooltip>
				<HStack justify={"end"} align="end" w="full">
					{file && <FileInfo file={file} />}
				</HStack>
			</HStack>
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
