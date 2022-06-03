import {
	AlertDialog as ChakraAlertDialog,
	AlertDialogBody,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogContent,
	AlertDialogOverlay,
	AlertDialogCloseButton,
} from "@chakra-ui/react";

export default function AlertDialog({
	isOpen = false,
	onOpen = () => ({}),
	onClose = () => ({}),
	children,
	footer,
	header,
}) {
	return (
		<>
			<ChakraAlertDialog
				motionPreset="slideInBottom"
				isOpen={isOpen}
				onClose={onClose}
				isCentered>
				<AlertDialogOverlay />
				<AlertDialogContent>
					<AlertDialogHeader>{header}</AlertDialogHeader>
					<AlertDialogCloseButton />
					<AlertDialogBody>{children}</AlertDialogBody>

					<AlertDialogFooter>{footer}</AlertDialogFooter>
				</AlertDialogContent>
			</ChakraAlertDialog>
		</>
	);
}
