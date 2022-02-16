import Router from "next/router";

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
	Stack,
} from "@chakra-ui/react";

import { HiMenuAlt3 } from "react-icons/hi";

export function TabsDrawer({
	tabs = [
		/* 
		{
			label: "<Name>",
			path: "<URL pathname>"
			icon: <Icon Component>
		} 
		
		*/
	],
}) {
	const { isOpen, onOpen, onClose } = useDisclosure();

	return (
		<>
			<IconButton
				icon={<HiMenuAlt3 size='1.4em' />}
				onClick={onOpen}
				variant='ghost'
				size='sm'
			/>

			<Drawer isOpen={isOpen} placement='right' onClose={onClose}>
				<DrawerOverlay />
				<DrawerContent>
					<DrawerCloseButton />
					<DrawerHeader>Navigation</DrawerHeader>

					<DrawerBody>
						<Stack>
							{tabs.map((tab) => (
								<Button
									leftIcon={tab.icon}
									variant={"ghost"}
									w='full'
									textTransform='capitalize'
									onClick={() => Router.push(tab.path)}>
									{tab.label}
								</Button>
							))}
						</Stack>
					</DrawerBody>

					<DrawerFooter>
						<Button variant='ghost' mr={3} onClick={onClose}>
							Close
						</Button>
					</DrawerFooter>
				</DrawerContent>
			</Drawer>
		</>
	);
}
