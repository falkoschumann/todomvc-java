import { MessageHandling } from '../domain/MessageHandling';
import {
  ClearCompletedCommand,
  CommandStatus,
  DestroyCommand,
  EditCommand,
  NewTodoCommand,
  ToggleAllCommand,
  ToggleCommand,
} from '../domain/messages/commands';
import { TodosQuery, TodosQueryResult } from '../domain/messages/queries';
import { HttpJsonClient } from './HttpJsonClient';

const BACKEND_URL = '/api/';

export class BackendProxy implements MessageHandling {
  private client = new HttpJsonClient(BACKEND_URL);

  handleClearCompletedCommand(command: ClearCompletedCommand): Promise<CommandStatus> {
    return this.client.execute('clear-completed-command', command);
  }

  handleDestroyCommand(command: DestroyCommand): Promise<CommandStatus> {
    return this.client.execute('destroy-command', command);
  }

  handleEditCommand(command: EditCommand): Promise<CommandStatus> {
    return this.client.execute('edit-command', command);
  }

  handleNewTodoCommand(command: NewTodoCommand): Promise<CommandStatus> {
    return this.client.execute('new-todo-command', command);
  }

  handleToggleAllCommand(command: ToggleAllCommand): Promise<CommandStatus> {
    return this.client.execute('toggle-all-command', command);
  }

  handleToggleCommand(command: ToggleCommand): Promise<CommandStatus> {
    return this.client.execute('toggle-command', command);
  }

  handleTodosQuery(query: TodosQuery): Promise<TodosQueryResult> {
    return this.client.execute('todos-query', query);
  }
}
