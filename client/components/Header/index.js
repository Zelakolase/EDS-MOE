import { getDataFromAPI } from "@API";
import { useEffect } from "react";
import { useRouter } from "next/router";
import { useTheme, MINI_WIDTH_SCREEN } from "@Theme";
import { LOGIN_PATHNAME } from "@CONSTANTS";
import { useWindowSize } from "rooks";
import {
	HStack,
	Button,
	Text,
	Heading,
	useColorMode,
	IconButton,
	Box,
	Flex,
	SimpleGrid,
	GridItem,
} from "@chakra-ui/react";

import { Logo } from "@Components";
import { TabsDrawer } from "./TabsDrawer";

import { MdSupportAgent } from "react-icons/md";
import { BiHomeAlt, BiInfoCircle, BiMoon } from "react-icons/bi";
import { BsSun } from "react-icons/bs";
import { AiOutlineTool } from "react-icons/ai";
import { useState } from "react";

export function Header() {
	const [bannerName, setBannerName] = useState("Electronic Document System");

	const router = useRouter();
	const { innerWidth } = useWindowSize();
	const { bg, color, bgHover } = useTheme();

	const tabs = [
		{ label: "home", path: "/", icon: <BiHomeAlt size='1.4em' /> },
		{ label: "tools", path: "/tools", icon: <AiOutlineTool size='1.4em' /> },
		{ label: "about", path: "/about", icon: <BiInfoCircle size='1.4em' /> },
		{
			label: "support",
			path: "/support",
			icon: <MdSupportAgent size='1.4em' />,
		},
	];
	useEffect(async () => {
		try {
			setBannerName((await getDataFromAPI("name"))?.name);
		} catch (err) {
			console.log(err);
		}
	});

	return (
		<Flex justify={"space-between"} w='full' px={2} py={6}>
			<HStack w='max-content' spacing={6}>
				{router.pathname !== LOGIN_PATHNAME && (
					<>
						<Logo />
						<Heading maxW={"120px"} size='md'>
							{bannerName ?? "Electronic Document System"}
						</Heading>
					</>
				)}
			</HStack>
			<HStack spacing={4}>
				{innerWidth > MINI_WIDTH_SCREEN ? (
					<Toolbar tabs={tabs} />
				) : (
					<TabsDrawer tabs={tabs} />
				)}
			</HStack>
		</Flex>
	);
}

function Toolbar({ tabs }) {
	const { toggleColorMode, colorMode } = useColorMode();
	const router = useRouter();
	const { bg, color, bgHover } = useTheme();
	return (
		<>
			{tabs.map((tab, index) => (
				<Button
					variant='ghost'
					key={index}
					size='sm'
					textTransform='capitalize'
					leftIcon={tab.icon}
					onClick={() => router.push(tab.path)}>
					{tab.label}
				</Button>
			))}
			<HStack spacing={1}>
				<Button
					alignItems='center'
					bgColor={bg}
					color={color}
					_hover={{ bgColor: bgHover }}
					mr={1}
					onClick={() => router.push(LOGIN_PATHNAME)}
					size='sm'>
					Login
				</Button>
				<IconButton
					maxW={20}
					onClick={toggleColorMode}
					size='sm'
					bgColor={bg}
					color={color}
					_hover={{ bgColor: bgHover }}
					colorScheme='gray'
					icon={
						colorMode === "light" ? (
							<BiMoon size='1.5em' />
						) : (
							<BsSun size='1.5em' />
						)
					}
				/>
			</HStack>
		</>
	);
}
