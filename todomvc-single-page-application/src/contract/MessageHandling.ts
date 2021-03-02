import {
  ClearCompletedCommand,
  CommandStatus,
  DestroyCommand,
  EditCommand,
  NewTodoCommand,
  ToggleAllCommand,
  ToggleCommand,
} from './messages/commands';
import { TodosQuery, TodosQueryResult } from './messages/queries';

export interface MessageHandling {
  handleClearCompletedCommand(command: ClearCompletedCommand): Promise<CommandStatus>;
  handleDestroyCommand(command: DestroyCommand): Promise<CommandStatus>;
  handleEditCommand(command: EditCommand): Promise<CommandStatus>;
  handleNewTodoCommand(command: NewTodoCommand): Promise<CommandStatus>;
  handleToggleAllCommand(command: ToggleAllCommand): Promise<CommandStatus>;
  handleToggleCommand(command: ToggleCommand): Promise<CommandStatus>;
  handleTodosQuery(query: TodosQuery): Promise<TodosQueryResult>;
}
