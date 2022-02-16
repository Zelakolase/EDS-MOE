import { TEAM_NAME } from "@CONSTANTS";

import { SimpleGrid, Center, Box, Button, Flex } from "@chakra-ui/react";
import { motion } from "framer-motion";

export function Logo({
	animationDuration = 6 /* For normal cases */,
	delayAnimationDuration = 0,
	inline = false /* Make logo characters in one line */,
}) {
	return (
		<SimpleGrid columns={inline ? 4 : 2} spacing={1}>
			{TEAM_NAME.split("").map((c, index) => (
				<Center key={index} position='relative'>
					<motion.div
						animate={{ rotate: 360 * (index % 2 ? 1 : -1) }}
						transition={{
							duration: animationDuration,
							repeat: Infinity,
							repeatType: "mirror",
							repeatDelay: delayAnimationDuration,
						}}>
						<Box
							h={6}
							w={6}
							borderRadius={6}
							bgColor='gray.900'
							transitionDuration='.2s'
							_hover={{ bgColor: "gray.700" }}
						/>
					</motion.div>
					<Box pt={1} textColor={"white"} position='absolute' userSelect='none'>
						{c}
					</Box>
				</Center>
			))}
		</SimpleGrid>
	);
}
