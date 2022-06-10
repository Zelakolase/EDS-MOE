import { request } from "@API";
import { useState, useContext, createContext, useRef, useEffect } from "react";
import { useTheme } from "@Theme";
import { useContextState } from "@Hooks";

import { FileInfo, Modal } from "@Components";

import {
	Button,
	Menu,
	MenuButton,
	MenuList,
	MenuItem,
	MenuItemOption,
	MenuGroup,
	MenuOptionGroup,
	MenuDivider,
	Stack,
	Heading,
	FormControl,
	FormLabel,
	FormErrorMessage,
	FormHelperText,
	Input,
	Box,
	Tooltip,
	HStack,
	IconButton,
	useDisclosure,
	useToast,
	Divider,
	Text,
	Alert,
	AlertIcon,
} from "@chakra-ui/react";

import {
	AiOutlineArrowDown,
	AiOutlineDownload,
	AiOutlineSearch,
} from "react-icons/ai";
import { MdOutlineVerified } from "react-icons/md";
import { HiOutlineDocumentDownload } from "react-icons/hi";
import { RiFolderInfoLine } from "react-icons/ri";
import { useRouter } from "next/router";

const ToolsContext = createContext();

function Tools() {
	const [state, stateSetter] = useContextState({
		downloadDocument: {
			publicCode: "",
		},
		verifyDocument: {
			file: null,
			verifyCode: "",
		},
		searchDocument: {
			publicCode: "",
		},
	});

	const tools = [
		{
			label: "Download a document",
			component: <DownloadDocumentComponent />,
		},
		{
			label: "Verify a document",
			component: <VerifyDocumentComponent />,
		},
		{
			label: "Search for a document",
			component: <SearchDocumentComponent />,
		},
	];
	const [currentTool, setCurrentTool] = useState(0);
	const btnRef = useRef(null);

	const { bg, color, bgHover } = useTheme();

	useEffect(() => {
		const keyDownHandler = e => {
			switch (e.key) {
				case "Enter":
					btnRef.current.click();
					break;

				default:
					break;
			}
		};
		window.addEventListener("keydown", keyDownHandler);

		return () => window.removeEventListener("keydown", keyDownHandler);
	}, []);

	return (
		<ToolsContext.Provider value={{ ...state, stateSetter, btnRef }}>
			<Stack align="center" spacing={10} justify="center" h="full">
				<ToolsMenu
					tools={tools}
					currentTool={currentTool}
					setCurrentTool={setCurrentTool}
				/>
				{tools[currentTool] && tools[currentTool]?.component}
			</Stack>
		</ToolsContext.Provider>
	);
}

function ToolsMenu({ tools = [], setCurrentTool, currentTool = 0 }) {
	const { bg, color, bgHover } = useTheme();

	return (
		<>
			<Menu>
				<MenuButton
					alignItems="center"
					bgColor={bg}
					size="lg"
					color={color}
					_hover={{ bgColor: bgHover }}
					_expanded={{ bgColor: bgHover }}
					_focus={{ boxShadow: "outline" }}
					as={Button}
					rightIcon={<AiOutlineArrowDown size="1.4em" />}>
					{tools[currentTool] ? tools[currentTool].label : "Tools"}
				</MenuButton>
				<MenuList>
					{tools.map((tool, index) => (
						<MenuItem
							onClick={() => setCurrentTool(index)}
							key={index}>
							{tool.label}
						</MenuItem>
					))}
				</MenuList>
			</Menu>
		</>
	);
}

function DownloadDocumentComponent() {
	const toast = useToast();

	const [loading, setLoading] = useState(false);

	const { downloadDocument, stateSetter, btnRef } = useContext(ToolsContext);
	const { publicCode } = downloadDocument;

	async function downloadHandler() {
		setLoading(true);
		try {
			const response = await request("post", "dac")(
				{
					public_code: publicCode,
				},
				null,
				{
					responseType: "blob",
				}
			);
			if (response?.data.status === "failed") throw response.data.msg;

			if (response?.data === "error")
				throw "Something wrong, Please contact with support to solve this problem.";

			const link = document.createElement("a");

			link.target = "_blank";
			link.download = `${publicCode}.${response.headers["content-type"]}`;
			link.href = URL.createObjectURL(response.data);

			document.body.appendChild(link);
			link.click();
			link.parentNode.removeChild(link);
		} catch (err) {
			toast({
				title: "Search failed.",
				description: err.toString(),
				status: "error",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
			console.error(err);
		}
		setLoading(false);
	}
	return (
		<>
			<Stack w="full" spacing={5} align={"center"}>
				<Stack>
					<Heading size="xs">Public Code</Heading>
					<Input
						value={publicCode}
						onChange={e =>
							stateSetter(
								"downloadDocument.publicCode",
								e.target.value
							)
						}
						size={"md"}
						variant={"filled"}
						placeholder="Code"
					/>
				</Stack>
				<Button
					ref={btnRef}
					isDisabled={!publicCode}
					isLoading={loading}
					onClick={async () => {
						await downloadHandler();
					}}
					maxW={"min-content"}
					rightIcon={<AiOutlineDownload size="1.4em" />}>
					Download
				</Button>
			</Stack>
		</>
	);
}

function VerifyDocumentComponent() {
	const toast = useToast();
	const [loading, setLoading] = useState(false);
	const [result, setResult] = useState();

	const { verifyDocument, stateSetter, btnRef } = useContext(ToolsContext);
	const { verifyCode, file } = verifyDocument;

	async function verifyHandler() {
		setLoading(true);
		try {
			let arrBuf = await file.arrayBuffer();

			const response = await request("post", "vad")(
				new Uint8Array(arrBuf),
				{
					"Content-Type": "application/pdf",
					verify_code: verifyCode,
				}
			);
			if (response?.data.status === "failed") throw response.data.msg;
			if (response?.data === "error")
				throw "Something wrong, Please try again or contact with support to solve this problem.";

			setResult(response.data);
			toast({
				title: "Result.",
				description: response.data.msg,
				status: "info",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
		} catch (err) {
			toast({
				title: "Verify failed.",
				description: err.toString(),
				status: "error",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
		}
		setLoading(false);
	}
	return (
		<Stack w="full" spacing={8} align={"center"}>
			<Stack>
				<Heading size="xs">Verification Code</Heading>
				<Input
					size={"md"}
					variant={"filled"}
					placeholder="Code"
					value={verifyCode}
					onChange={e => {
						stateSetter(
							"verifyDocument.verifyCode",
							e.target.value
						);
						// console.log(verifyCode);
					}}
				/>
				<HStack justify={"end"}>
					{file && <FileInfo file={file} />}
					<Tooltip
						hasArrow
						placement="top"
						label="Import the document which you need to verify">
						<Box position="relative" w={"max-content"}>
							<Button
								size="sm"
								rightIcon={
									<HiOutlineDocumentDownload size="1.4em" />
								}>
								Import File
							</Button>
							<Input
								w="full"
								type={"file"}
								accept=".pdf"
								left={0}
								onChange={async e => {
									let __file__ = e.target.files[0];
									if (typeof __file__ !== "undefined") {
										stateSetter(
											"verifyDocument.file",
											__file__
										);
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
				</HStack>
			</Stack>
			<Button
				ref={btnRef}
				onClick={async () => {
					await verifyHandler();
				}}
				isDisabled={!file || !verifyCode}
				isLoading={loading}
				maxW={"min-content"}
				rightIcon={<MdOutlineVerified size="1.4em" />}>
				Verify
			</Button>
		</Stack>
	);
}

function SearchDocumentComponent() {
	const toast = useToast();
	const router = useRouter();

	const [result, setResult] = useState({});

	const [loading, setLoading] = useState(false);

	const disclosure = useDisclosure();
	const { onOpen, onClose } = disclosure;

	const { searchDocument, stateSetter, btnRef } = useContext(ToolsContext);
	const { publicCode } = searchDocument;

	async function searchHandler() {
		setLoading(true);
		try {
			const response = await request(
				"post",
				"sfad"
			)({
				public_code: publicCode,
			});
			if (response?.data?.status === "failed") throw response?.data?.msg;
			// console.log(response);
			setResult(response?.data);
			onOpen();
		} catch (err) {
			toast({
				title: "Search failed.",
				description: err.toString(),
				status: "error",
				position: "top",
				duration: 9000,
				isClosable: true,
			});
		}
		setLoading(false);
	}
	return (
		<>
			<Stack w="full" spacing={5} align={"center"}>
				<Stack>
					<Heading size="xs">Public Code</Heading>
					<Input
						value={publicCode}
						onChange={e =>
							stateSetter(
								"searchDocument.publicCode",
								e.target.value
							)
						}
						size={"md"}
						variant={"filled"}
						placeholder="Code"
					/>
				</Stack>
				<Button
					ref={btnRef}
					isDisabled={!publicCode}
					isLoading={loading}
					onClick={async () => {
						await searchHandler();
					}}
					maxW={"min-content"}
					rightIcon={<AiOutlineSearch size="1.4em" />}>
					Search
				</Button>
			</Stack>
			<Modal
				{...disclosure}
				header={`Search result `}
				footer={<Button onClick={onClose}>Close</Button>}>
				<Stack>
					{[
						"document_name",
						"verifier",
						"writer",
						"date_of_publication",
					].filter(e => !result.hasOwnProperty(e)).length > 0 && (
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

					<Divider />

					<Stack spacing={2}>
						<Stack spacing={0}>
							<Heading fontSize={12}>Document name</Heading>
							<Text>{result?.document_name ?? "Not found"}</Text>
						</Stack>
						<Stack spacing={0}>
							<Heading fontSize={12}>Verifier</Heading>
							<Text>{result?.verifier ?? "Not found"}</Text>
						</Stack>
						<Stack spacing={0}>
							<Heading fontSize={12}>Writer</Heading>
							<Text>{result?.writer ?? "Not found"}</Text>
						</Stack>
						<Stack spacing={0}>
							<Heading fontSize={12}>Date of publication</Heading>
							<Text>
								{result?.date_of_publication ?? "Not found"}
							</Text>
						</Stack>
					</Stack>
				</Stack>
			</Modal>
		</>
	);
}

export default Tools;
