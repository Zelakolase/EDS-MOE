import { useAuth } from "@Auth";
import { useRouter } from "next/router";
import {
	AlertDialog,
	AlertDialogBody,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogContent,
	AlertDialogOverlay,
	Button,
	useDisclosure,
} from "@chakra-ui/react";
export function LogoutModal({ children, style }) {
	const { signout } = useAuth();
	const router = useRouter();
	const { isOpen, onOpen, onClose } = useDisclosure();
	return (
		<>
			<div onClick={onOpen} style={style}>
				{children}
			</div>

			<AlertDialog
				isOpen={isOpen}
				onClose={onClose}
				motionPreset="slideInBottom"
				isCentered>
				<AlertDialogOverlay>
					<AlertDialogContent>
						<AlertDialogHeader fontSize="lg" fontWeight="bold">
							Log out
						</AlertDialogHeader>

						<AlertDialogBody>
							Do you really want to log out?
						</AlertDialogBody>

						<AlertDialogFooter>
							<Button onClick={onClose}>Cancel</Button>
							<Button
								colorScheme="red"
								onClick={() => {
									signout();
									onClose();
									router.push("/auth/login");
								}}
								ml={3}>
								Logout
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>
		</>
	);
}
