import { useColorModeValue } from "@chakra-ui/react"

function useTheme() {
	const bg = useColorModeValue("gray.900", "white")
	const bgHover = useColorModeValue("gray.700", "gray.300")
	const color = useColorModeValue("white", "gray.900")
	return { bg, color, bgHover }
}

const breakpoints = {
	xs: "320px",
	sm: "450px",
	md: "768px",
	lg: "960px",
	xl: "1200px",
	"2xl": "1536px",
}

const MINI_WIDTH_SCREEN = 801

export { useTheme, MINI_WIDTH_SCREEN, breakpoints }
