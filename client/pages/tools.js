import { request } from "@API";
import { useState, useContext, createContext } from "react";
import { useTheme } from "@Theme";

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
} from "@chakra-ui/react";

import {
	AiOutlineArrowDown,
	AiOutlineDownload,
	AiOutlineSearch,
} from "react-icons/ai";
import { MdOutlineVerified } from "react-icons/md";
import { HiOutlineDocumentDownload } from "react-icons/hi";
import { RiFolderInfoLine } from "react-icons/ri";

const ToolsContext = createContext();

function Tools() {
	const [state, setState] = useState({
		downloadDocument: {
			publicCode: "",
		},
		verifyDocument: {
			file: "",
			verifyCode: "",
		},
		searchDocument: {
			publicCode: "",
		},
	});

	function stateSetter(q) {
		let query = q.split(".").map(e => e.trim());

		return value => {
			setState({
				...state,
				[query[0]]: {
					...state[query[0]],
					[query[1]]: value,
				},
			});

			console.log(state);
		};
	}

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

	const { bg, color, bgHover } = useTheme();

	return (
		<ToolsContext.Provider value={{ ...state, stateSetter }}>
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

	const { downloadDocument, stateSetter } = useContext(ToolsContext);
	const { publicCode } = downloadDocument;

	// async
	async function downloadHandler() {
		setLoading(true);
		try {
			const response = await request(
				"post",
				"dac"
			)({
				public_code: publicCode,
			});
			if (response?.data.status === "failed") throw response.data.msg;
			if (response?.data === "error")
				throw "Something wrong, Please contact with support to solve this problem.";
			// console.log(data);
			setResult(data.data);
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
							stateSetter("downloadDocument.publicCode")(
								e.target.value
							)
						}
						size={"md"}
						variant={"filled"}
						placeholder="Code"
					/>
				</Stack>
				<Button
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
	// const [file, setFile] = useState();
	// const [verifyCode, setVerifyCode] = useState("");

	const { verifyDocument, stateSetter } = useContext(ToolsContext);
	const { verifyCode, file } = verifyDocument;
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
						stateSetter("verifyDocument.verifyCode")(
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
								left={0}
								onChange={e => {
									stateSetter("verifyDocument.file")(
										e.target.files[0]
									);
									// console.log(verifyDocument);
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
				isDisabled={!file || !verifyCode}
				maxW={"min-content"}
				rightIcon={<MdOutlineVerified size="1.4em" />}>
				Verify
			</Button>
		</Stack>
	);
}

function SearchDocumentComponent() {
	const [result, setResult] = useState(null);

	const [loading, setLoading] = useState(false);
	const toast = useToast();

	const disclosure = useDisclosure();
	const { onOpen, onClose } = disclosure;

	const { searchDocument, stateSetter } = useContext(ToolsContext);
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
							stateSetter("searchDocument.publicCode")(
								e.target.value
							)
						}
						size={"md"}
						variant={"filled"}
						placeholder="Code"
					/>
				</Stack>
				<Button
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
					<Divider />
					<Stack spacing={2}>
						<Stack spacing={0}>
							<Heading fontSize={12}>Document name</Heading>
							<Text>
								{result?.document_name ?? "[Document name]"}
							</Text>
						</Stack>
						<Stack spacing={0}>
							<Heading fontSize={12}>Verifier</Heading>
							<Text>{result?.verifier ?? "[Verifier]"}</Text>
						</Stack>
						<Stack spacing={0}>
							<Heading fontSize={12}>Writer</Heading>
							<Text>{result?.writer ?? "[Writer]"}</Text>
						</Stack>
						<Stack spacing={0}>
							<Heading fontSize={12}>Date of publication</Heading>
							<Text>
								{result?.date_of_publication ??
									"[date_of_publication]"}
							</Text>
						</Stack>
					</Stack>
				</Stack>
			</Modal>
		</>
	);
}

export default Tools;
