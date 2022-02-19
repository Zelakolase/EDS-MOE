import { useRouter } from "next/router";
import { motion } from "framer-motion";

import { Header } from "@Components";

import { Stack } from "@chakra-ui/react";
export function PageLayout({ children, isLoading }) {
	const Router = useRouter();

	// Variants for farmer motion
	const variants = {
		hidden: { opacity: 0, x: -200, y: 0 },
		enter: { opacity: 1, x: 0, y: 0 },
		exit: { opacity: 0, x: 0, y: -100 },
	};

	return (
		<motion.div
			style={{ height: "100vh", width: "100vw" }}
			variants={variants} // Pass the variant object into Framer Motion
			initial='hidden' // Set the initial state to variants.hidden
			animate='enter' // Animated state to variants.enter
			exit='exit' // Exit state (used later) to variants.exit
			transition={{ type: "linear" }} // Set the transition to linear
		>
			<Stack align='center' px={4} h='full'>
				<Header />
				{children}
			</Stack>
		</motion.div>
	);
}
