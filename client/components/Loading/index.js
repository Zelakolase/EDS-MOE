import { Logo } from "@Components";
import { Box, Button, Stack, Text } from "@chakra-ui/react";
import { motion } from "framer-motion";
export function Loading() {
	return (
		<Stack>
			<motion.div
				transition={{
					duration: 0.5,
					repeat: Infinity,
					repeatType: "mirror",
				}}
				initial={{ opacity: 0.8 }}
				animate={{ opacity: 1 }}>
				<Stack align='center'>
					<Box fontSize={"sm"}>Loading...</Box>
					<Text fontSize='xs'>Please wait few seconds.</Text>
				</Stack>
			</motion.div>
			<Logo animationDuration={1} inline />
		</Stack>
	);
}
