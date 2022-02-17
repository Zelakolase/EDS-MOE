import Router from "next/router";
import { useTheme } from "@Theme";
import { useWindowSize } from "rooks";
import {
	HStack,
	Button,
	Text,
	Heading,
	useColorMode,
	useColorModeValue,
	IconButton,
} from "@chakra-ui/react";

import { Logo } from "@Components";
import { TabsDrawer } from "./TabsDrawer";

import { MdSupportAgent } from "react-icons/md";
import { BiHomeAlt, BiInfoCircle } from "react-icons/bi";
import { BsSun, BiMoon } from "react-icons/bs";
import { AiOutlineTool } from "react-icons/ai";

export function Header() {
	const { toggleColorMode } = useColorMode();
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
	return (
		<HStack justify={"space-between"} w='full' px={4} py={6}>
			{innerWidth < 768 && <div />}
			<HStack w='max-content'>
				<Logo delayAnimationDuration={2} />
				<Heading size='xs'>Omar ElFarouk G.L.S</Heading>
			</HStack>
			<HStack spacing={4}>
				{innerWidth > 768 ? (
					<>
						{tabs.map((tab, index) => (
							<Button
								variant='ghost'
								key={index}
								size='sm'
								textTransform='capitalize'
								leftIcon={tab.icon}
								onClick={() => Router.push(tab.path)}>
								{tab.label}
							</Button>
						))}
						<HStack>
							<Button
								alignItems='center'
								bgColor={bg}
								color={color}
								_hover={{ bgColor: bgHover }}
								onClick={() => Router.push("/login")}
								size='sm'>
								Login
							</Button>
							<IconButton
								onClick={toggleColorMode}
								size='sm'
								bgColor={bg}
								color={color}
								_hover={{ bgColor: bgHover }}
								colorScheme='gray'
								icon={<BsSun size='1.2em' />}
							/>
						</HStack>
					</>
				) : (
					<TabsDrawer tabs={tabs} />
				)}
			</HStack>
		</HStack>
	);
}
