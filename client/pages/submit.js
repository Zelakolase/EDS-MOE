import { useState, useEffect, useContext, createContext, useRef } from "react";
import { useRouter } from "next/router";

import { useAuth } from "@Auth";
import { request } from "@API";
import { useContextState } from "@Hooks";

import { useWindowSize } from "rooks";
import { MINI_WIDTH_SCREEN } from "@Theme";
import { Logo, FileInfo, AlertDialog } from "@Components";
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
	Alert,
	AlertIcon,
	useDisclosure,
} from "@chakra-ui/react";
import { Step, Steps, useSteps } from "chakra-ui-steps";

import { AiOutlineArrowRight, AiOutlineUpload } from "react-icons/ai";
import { RiFolderInfoLine } from "react-icons/ri";
import { MdDateRange } from "react-icons/md";
import { BiHomeAlt } from "react-icons/bi";
import { HiOutlineDocumentAdd } from "react-icons/hi";

const OperaionContext = createContext();

const useOperation = () => useContext(OperaionContext);

export default function Operation() {
	const { isAuth, username, sessionID } = useAuth();
	const disclosure = useDisclosure();

	const router = useRouter();
	const toast = useToast();

	const { innerWidth } = useWindowSize();
	const {
		nextStep,
		prevStep,
		setStep,
		reset: stepsResetter,
		activeStep,
	} = useSteps({
		initialStep: 0,
	});

	// States
	const [isNextButtonDisabled, setIsNextButtonDisabled] = useState(false);
	const [isNextButtonLoading, setIsNextButtonLoading] = useState(false);
	const [generatedDocResult, setGenerateDocResult] = useState(null);
	const [uploadDocResult, setuploadDocResult] = useState(null);

	const [state, stateSetter, stateResetter] = useContextState({
		generateDocument: {
			document_name: "",
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
	const lastStep = steps.length - 1;

	async function generateDoc() {
		setIsNextButtonLoading(true);
		try {
			// Destructing
			const { generateDocument } = state;
			const { document_name: doc_name, writer } = generateDocument;

			const response = await request(
				"post",
				"generate"
			)({ writer, session_id: sessionID, doc_name });
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
	const uploadDoc = async () => {
		setIsNextButtonLoading(true);

		try {
			if (state.upload.file === null)
				throw "You should use a file to upload it.";
			if (typeof generatedDocResult.verify_code === "undefined")
				throw "Verify code not found.";

			const { file } = state.upload;
			let arrBuf = await file.arrayBuffer();

			let __file__ = {
				buffer: new Uint8Array(arrBuf),
				extension: file.name.split(".").at(-1),
			};

			const response = await request("post", "doc")(__file__.buffer, {
				extension: __file__.extension,
				verfiy_code: generatedDocResult?.verify_code,
				session_id: sessionID,
				"Content-type": "application/text",
			});

			if (response?.data?.status === "failed") throw response?.data?.msg;

			setuploadDocResult(response?.data);
			nextStep();
		} catch (err) {
			console.error(err);
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
	};
	useEffect(() => {
		if (!isAuth) router.replace("/auth/login");
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
				stateResetter,
				stepsResetter,
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
									case lastStep:
										disclosure.onOpen();
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
			<FinishAlert disclosure={disclosure} />
		</OperaionContext.Provider>
	);
}

function GenerateDocument() {
	const { document_name, writer, stateSetter, setIsNextButtonDisabled } =
		useOperation();

	useEffect(() => {
		if ([document_name, writer].filter(e => !e).length > 0)
			setIsNextButtonDisabled(true);
		else setIsNextButtonDisabled(false);
	}, [document_name, writer]);

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
	const { stateSetter, file, setIsNextButtonDisabled, generatedDocResult } =
		useOperation();

	const buttonSize = useBreakpointValue({
		xs: "sm",
		md: "lg",
	});

	useEffect(() => {
		if (file === null) setIsNextButtonDisabled(true);
		else setIsNextButtonDisabled(false);
	}, [file]);

	return (
		<>
			{!generatedDocResult.verify_code && (
				<Alert status="error">
					<AlertIcon />
					<div>
						Verify code not found, Please{" "}
						<span
							style={{
								textDecoration: "underline",
								cursor: "pointer",
							}}
							onClick={() => router.push("/support")}>
							contact us
						</span>{" "}
						to solve this problem.
					</div>
				</Alert>
			)}
			<Stack
				w="full"
				h="full"
				spacing={6}
				justify="center"
				align={"center"}>
				{generatedDocResult.verify_code && (
					<Stack spacing={0}>
						<Heading opacity={0.5} fontSize="x-small">
							Verify code
						</Heading>
						<Heading>{generatedDocResult["verify_code"]}</Heading>
					</Stack>
				)}
				<Stack align={"center"}>
					<Text>
						Please, Insert Verification code inside the document,
						then upload it below.
					</Text>
					<HStack>
						<Tooltip
							hasArrow
							placement="top"
							label="Import the document which you need to verify">
							<Box position="relative" w={"max-content"}>
								<Button
									size={buttonSize}
									rightIcon={
										<AiOutlineUpload size="1.4em" />
									}>
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
										if (typeof __file__ !== "undefined") {
											stateSetter(
												"upload.file",
												__file__
											);
											// stateSetter("upload.file", __file__);
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
			</Stack>
		</>
	);
}

function Done() {
	const router = useRouter();
	const { uploadDocResult } = useOperation();
	return (
		<Stack align={"center"}>
			{["verify_code", "public_code"].filter(
				e => !uploadDocResult.hasOwnProperty(e) && e.trim().length > 0
			).length > 0 && (
				<Alert status="error">
					<AlertIcon />
					<div>
						Some data isn't found, Please{" "}
						<span
							style={{
								textDecoration: "underline",
								cursor: "pointer",
							}}
							onClick={() => router.push("/support")}>
							contact us
						</span>{" "}
						to solve this problem.
					</div>
				</Alert>
			)}
			<Text fontSize={18}>The document is uploaded successfully !</Text>
			<Divider />
			<Stack>
				{["public_code", "verify_code"].map(e => (
					<Stack spacing={0.5}>
						<Heading
							opacity={0.5}
							fontSize={10}
							textTransform="capitalize">
							{e.replace(/_/g, " ")}
						</Heading>
						<Heading size="lg">
							{uploadDocResult[e] || "Error: Not found"}
						</Heading>
					</Stack>
				))}
			</Stack>
		</Stack>
	);
}

function FinishAlert({ disclosure }) {
	const router = useRouter();
	const { stateResetter, stepsResetter } = useOperation();
	return (
		<AlertDialog
			{...disclosure}
			header={`Submit more? `}
			footer={
				<HStack justify={"space-between"} w="full">
					<Button
						leftIcon={<BiHomeAlt size="1.4em" />}
						size="sm"
						onClick={() => router.push("/")}>
						Back Home
					</Button>
					<Button
						size="sm"
						leftIcon={<HiOutlineDocumentAdd size="1.4em" />}
						onClick={() => {
							stateResetter();
							stepsResetter();
							disclosure.onClose();
						}}>
						Submit more
					</Button>
				</HStack>
			}>
			<Text>
				Do you want to submit more documents or that{"'"}s enough for
				this day?
			</Text>
		</AlertDialog>
	);
}
