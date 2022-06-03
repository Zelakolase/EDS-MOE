import {
	Modal as ChakraModal,
	ModalOverlay,
	ModalContent,
	ModalHeader,
	ModalFooter,
	ModalBody,
	ModalCloseButton,
	useDisclosure,
} from "@chakra-ui/react";

export default function Modal({
	isOpen = false,
	onOpen = () => ({}),
	onClose = () => ({}),
	children,
	footer,
	header,
}) {
	return (
		<>
			<ChakraModal isOpen={isOpen} onClose={onClose} isCentered>
				<ModalOverlay />
				<ModalContent>
					<ModalHeader>{header}</ModalHeader>
					<ModalCloseButton />
					<ModalBody>{children}</ModalBody>

					<ModalFooter>{footer}</ModalFooter>
				</ModalContent>
			</ChakraModal>
		</>
	);
}
