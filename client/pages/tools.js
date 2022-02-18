import { useState } from "react";
import { useTheme } from "@Theme";
import { css } from "@emotion/react";

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
} from "@chakra-ui/react";

import {
	AiOutlineArrowDown,
	AiOutlineDownload,
	AiOutlineSearch,
} from "react-icons/ai";
import { MdOutlineVerified } from "react-icons/md";
import { HiOutlineDocumentDownload } from "react-icons/hi";
import { RiFolderInfoLine } from "react-icons/ri";

function Tools() {
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
		<Stack align='center' spacing={10} justify='center' h='full'>
			<ToolsMenu
				tools={tools}
				currentTool={currentTool}
				setCurrentTool={setCurrentTool}
			/>
			{tools[currentTool] && tools[currentTool]?.component}
		</Stack>
	);
}

export default Tools;

function ToolsMenu({ tools = [], setCurrentTool, currentTool = 0 }) {
	const { bg, color, bgHover } = useTheme();

	return (
		<>
			<Menu>
				<MenuButton
					alignItems='center'
					bgColor={bg}
					size='lg'
					color={color}
					_hover={{ bgColor: bgHover }}
					_expanded={{ bgColor: bgHover }}
					_focus={{ boxShadow: "outline" }}
					as={Button}
					rightIcon={<AiOutlineArrowDown size='1.4em' />}>
					{tools[currentTool] ? tools[currentTool].label : "Tools"}
				</MenuButton>
				<MenuList>
					{tools.map((tool, index) => (
						<MenuItem onClick={() => setCurrentTool(index)} key={index}>
							{tool.label}
						</MenuItem>
					))}
				</MenuList>
			</Menu>
		</>
	);
}

function DownloadDocumentComponent() {
	const [publicCode, setPublicCode] = useState("");

	return (
		<>
			<Stack w='full' spacing={5} align={"center"}>
				<Stack>
					<Heading size='xs'>Public Code</Heading>
					<Input
						value={publicCode}
						onChange={(e) => setPublicCode(e.target.kvalue)}
						size={"md"}
						variant={"filled"}
						placeholder='Code'
					/>
				</Stack>
				<Button
					maxW={"min-content"}
					rightIcon={<AiOutlineDownload size='1.4em' />}>
					Download
				</Button>
			</Stack>
		</>
	);
}

function VerifyDocumentComponent() {
	const [file, setFile] = useState();

	return (
		<>
			<Stack w='full' spacing={8} align={"center"}>
				<Stack>
					<Heading size='xs'>Verification Code</Heading>
					<Input size={"md"} variant={"filled"} placeholder='Code' />
					<HStack justify={"end"}>
						{file && (
							<Tooltip placement='top' hasArrow label={file?.name ?? ""}>
								<IconButton
									size='sm'
									icon={<RiFolderInfoLine size='1.4em' />}
								/>
							</Tooltip>
						)}
						<Tooltip
							hasArrow
							placement='top'
							label='Import the document which you need to verify'>
							<Box position='relative' w={"max-content"}>
								<Button
									size='sm'
									rightIcon={<HiOutlineDocumentDownload size='1.4em' />}>
									Import File
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
					</HStack>
				</Stack>
				<Button
					maxW={"min-content"}
					rightIcon={<MdOutlineVerified size='1.4em' />}>
					Verify
				</Button>
			</Stack>
		</>
	);
}

function SearchDocumentComponent() {
	const [publicCode, setPublicCode] = useState("");
	return (
		<>
			<Stack w='full' spacing={5} align={"center"}>
				<Stack>
					<Heading size='xs'>Public Code</Heading>
					<Input
						value={publicCode}
						onChange={(e) => setPublicCode(e.target.value)}
						size={"md"}
						variant={"filled"}
						placeholder='Code'
					/>
				</Stack>
				<Button
					maxW={"min-content"}
					rightIcon={<AiOutlineSearch size='1.4em' />}>
					Search
				</Button>
			</Stack>
		</>
	);
}
