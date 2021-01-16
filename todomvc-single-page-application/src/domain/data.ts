export type TodoId = string;

export type Todo = Readonly<{
  id: TodoId;
  title: string;
  completed: boolean;
}>;
