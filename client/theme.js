import { useColorModeValue } from "@chakra-ui/react";

export function useTheme() {
	const bg = useColorModeValue("gray.900", "white");
	const bgHover = useColorModeValue("gray.700", "gray.300");
	const color = useColorModeValue("white", "gray.900");
	return { bg, color, bgHover };
}
