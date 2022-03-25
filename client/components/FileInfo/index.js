import {
	Popover,
	PopoverTrigger,
	PopoverContent,
	PopoverHeader,
	PopoverBody,
	PopoverFooter,
	PopoverArrow,
	PopoverCloseButton,
	PopoverAnchor,
	IconButton,
	HStack,
	Heading,
	Box,
	Stack,
} from "@chakra-ui/react";

import { RiFolderInfoLine } from "react-icons/ri";
import { FiHardDrive } from "react-icons/fi";
import { AiOutlineFile } from "react-icons/ai";
export function FileInfo({ file = { name: null, size: null } }) {
	return (
		<>
			<Popover>
				<PopoverTrigger>
					<IconButton size='sm' icon={<RiFolderInfoLine size='1.4em' />} />
				</PopoverTrigger>
				<PopoverContent>
					<PopoverArrow />
					<PopoverCloseButton />

					<PopoverBody>
						<Stack>
							<HStack>
								<AiOutlineFile />
								<Heading fontSize={14}>{file.name}</Heading>
							</HStack>
							<HStack>
								<FiHardDrive />
								<HStack fontSize={14} spacing={0.5}>
									<Heading size='xs'>
										{(file.size / Math.pow(1024, 2)).toFixed(2)}
									</Heading>
									<Heading fontSize={10}>MB</Heading>
								</HStack>
							</HStack>
						</Stack>
					</PopoverBody>
				</PopoverContent>
			</Popover>
		</>
	);
}
