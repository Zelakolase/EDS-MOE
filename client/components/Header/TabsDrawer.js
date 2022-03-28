import { useRouter } from "next/router";
import { LOGIN_PATHNAME } from "@CONSTANTS";
import {
	Drawer,
	DrawerBody,
	DrawerFooter,
	DrawerHeader,
	DrawerOverlay,
	DrawerContent,
	DrawerCloseButton,
	IconButton,
	Button,
	useDisclosure,
	useColorMode,
	Stack,
	HStack,
} from "@chakra-ui/react";
import { LogoutModal } from "./LogoutModal";

import { useTheme } from "@Theme";
import { useAuth } from "@Auth";

import { HiMenuAlt3 } from "react-icons/hi";
import { BsSun } from "react-icons/bs";
import { BiMoon } from "react-icons/bi";
export function TabsDrawer({
	tabs = [
		/*{
			label: "<Name>",
			path: "<URL pathname>"
			icon: <Icon Component>
		}*/
	],
}) {
	const router = useRouter();
	const { isOpen, onOpen, onClose } = useDisclosure();
	const { toggleColorMode, colorMode } = useColorMode();

	const { bg, color, bgHover } = useTheme();
	const { isSignedIn } = useAuth();

	return (
		<>
			<IconButton
				icon={<HiMenuAlt3 size="1.4em" />}
				onClick={onOpen}
				variant="ghost"
				size="sm"
			/>

			<Drawer
				size={"xs"}
				isOpen={isOpen}
				placement="right"
				onClose={onClose}>
				<DrawerOverlay />
				<DrawerContent>
					<DrawerCloseButton />
					<DrawerHeader>Navigation</DrawerHeader>

					<DrawerBody>
						<Stack h="full" justify="space-between">
							<Stack>
								{tabs.map((tab, index) => (
									<Button
										key={index}
										leftIcon={tab.icon}
										variant={"ghost"}
										w="full"
										textTransform="capitalize"
										onClick={() => {
											router.push(tab.path);
											onClose();
										}}>
										{tab.label}
									</Button>
								))}
							</Stack>
							<HStack w="full">
								{!isSignedIn ? (
									<Button
										w="full"
										alignItems="center"
										bgColor={bg}
										color={color}
										_hover={{ bgColor: bgHover }}
										onClick={() =>
											router.push(LOGIN_PATHNAME)
										}
										size="sm">
										Login
									</Button>
								) : (
									<LogoutModal style={{ width: "100%" }}>
										<Button
											size="sm"
											w="full"
											alignItems="center"
											bgColor={bg}
											color={color}
											_hover={{ bgColor: bgHover }}>
											Logout
										</Button>
									</LogoutModal>
								)}
								<IconButton
									onClick={toggleColorMode}
									size="sm"
									bgColor={bg}
									color={color}
									_hover={{ bgColor: bgHover }}
									colorScheme="gray"
									icon={
										colorMode === "light" ? (
											<BiMoon size="1.2em" />
										) : (
											<BsSun size="1.2em" />
										)
									}
								/>
							</HStack>
						</Stack>
					</DrawerBody>

					<DrawerFooter>
						<Button variant="ghost" mr={3} onClick={onClose}>
							Close
						</Button>
					</DrawerFooter>
				</DrawerContent>
			</Drawer>
		</>
	);
}
