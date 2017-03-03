export default function reducer (
	state={
		items:[]
	}, action){
	
	switch (action.type){
		case "ADD_TODO":{
			return {...state, items: [...state.items, action.payload]}
		}

		case "REMOVE_TODO":{
			return {...state, items: state.items.filter((item) => (item.id != action.payload))}
		}
	}
	
	return state;
}