import { Todo } from '../data';

export type TodosQuery = Readonly<{}>;

export type TodosQueryResult = Readonly<{
  todos: readonly Todo[];
}>;
