import { TEAM_NAME } from "@CONSTANTS";
import { useTheme } from "@Theme";
import {
	SimpleGrid,
	Center,
	Box,
	Button,
	Flex,
	Heading,
} from "@chakra-ui/react";
import { motion } from "framer-motion";
export function Logo({
	animationDuration = 6 /* For normal cases */,
	delayAnimationDuration = 0,
	inline = false /* Make logo characters in one line */,
	isAnimated = false,
}) {
	const { bg, color, bgHover } = useTheme();

	return (
		<SimpleGrid w="70px" h={16} columns={inline ? 4 : 2} spacing={1}>
			{TEAM_NAME.split("").map((c, index) => (
				<Center key={index} position="relative">
					<motion.div
						animate={
							isAnimated
								? { rotate: 360 * (index % 2 ? 1 : -1) }
								: false
						}
						transition={{
							duration: animationDuration,
							repeat: Infinity,
							repeatType: "mirror",
							repeatDelay: delayAnimationDuration,
						}}>
						<Box
							h={8}
							w={8}
							borderRadius={6}
							bgColor={bg}
							transitionDuration=".3s"
							_hover={{ bgColor: bgHover, h: 10, w: 10 }}
						/>
					</motion.div>
					<Heading
						pointerEvents={"none"}
						pt={1}
						size="md"
						color={color}
						position="absolute"
						userSelect="none">
						{c}
					</Heading>
				</Center>
			))}
		</SimpleGrid>
	);
}
