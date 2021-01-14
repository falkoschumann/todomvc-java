import { TodoId } from '../data';

export type CommandStatus = {
  success: boolean;
  errorMessage?: string; // required if success=false
};

export type ClearCompletedCommand = Readonly<{}>;

export type DestroyCommand = Readonly<{
  id: TodoId;
}>;

export type EditCommand = Readonly<{
  id: TodoId;
  title: string;
}>;

export type NewTodoCommand = Readonly<{
  title: string;
}>;

export type ToggleAllCommand = Readonly<{
  completed: boolean;
}>;

export type ToggleCommand = Readonly<{
  id: TodoId;
}>;
