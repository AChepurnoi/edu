
let todoId = 0;

export function addTodo(text) {
  todoId = todoId + 1;
  return {
    type: "ADD_TODO",
    payload: {id: todoId, text}
  }
}

export function deleteTodo(id){
	return{
		type: "REMOVE_TODO",
		payload: id
	}
}


