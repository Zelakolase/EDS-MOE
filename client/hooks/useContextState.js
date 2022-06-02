const { useContext, useState } = require("react");

export function useContextState(defaultValue = null) {
	const [state, setState] = useState(defaultValue);
	function stateSetter(q, value) {
		let query = q.split(".").map(e => e.trim());

		return (value => {
			setState({
				...state,
				[query[0]]: {
					...state[query[0]],
					[query[1]]: value,
				},
			});
			console.debug(state);
			console.debug(value);
		})(value);
	}

	return [state, stateSetter];
}
