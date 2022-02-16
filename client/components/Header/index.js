import Router from "next/router";
import { useWindowSize } from "rooks";
import { HStack, Button, Text, Heading } from "@chakra-ui/react";

import { Logo } from "@Components";
import { TabsDrawer } from "./TabsDrawer";

import { MdSupportAgent } from "react-icons/md";
import { BiHomeAlt, BiInfoCircle } from "react-icons/bi";
import { AiOutlineTool } from "react-icons/ai";

export function Header() {
	const { innerWidth } = useWindowSize();
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
			<HStack>
				<Logo delayAnimationDuration={2} />
				<Heading size='xs'>Omar ElFarouk G.L.S</Heading>
			</HStack>
			<HStack spacing={4}>
				{innerWidth > 768 ? (
					tabs.map((tab, index) => (
						<Button
							variant='ghost'
							size='sm'
							textTransform='capitalize'
							leftIcon={tab.icon}
							onClick={() => Router.push(tab.path)}>
							{tab.label}
						</Button>
					))
				) : (
					<TabsDrawer tabs={tabs} />
				)}
				<Button
					alignItems='center'
					bgColor={"gray.900"}
					color='white'
					_hover={{ bgColor: "gray.700" }}
					onClick={() => Router.push("/login")}
					size='sm'>
					Join us
				</Button>
			</HStack>
		</HStack>
	);
}
