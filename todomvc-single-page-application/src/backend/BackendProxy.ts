import { MessageHandling } from '../contract/MessageHandling';
import {
  ClearCompletedCommand,
  CommandStatus,
  DestroyCommand,
  EditCommand,
  NewTodoCommand,
  ToggleAllCommand,
  ToggleCommand,
} from '../contract/messages/commands';
import { TodosQuery, TodosQueryResult } from '../contract/messages/queries';
import { HttpJsonClient } from './HttpJsonClient';

const BACKEND_URL = 'http://localhost:8080/api/';

export class BackendProxy implements MessageHandling {
  private client = new HttpJsonClient(BACKEND_URL);

  async handleClearCompletedCommand(command: ClearCompletedCommand): Promise<CommandStatus> {
    try {
      return await this.client.execute('clear-completed-command', command);
    } catch (error) {
      return { success: false, errorMessage: error };
    }
  }

  async handleDestroyCommand(command: DestroyCommand): Promise<CommandStatus> {
    try {
      return await this.client.execute('destroy-command', command);
    } catch (error) {
      return { success: false, errorMessage: error };
    }
  }

  async handleEditCommand(command: EditCommand): Promise<CommandStatus> {
    try {
      return await this.client.execute('edit-command', command);
    } catch (error) {
      return { success: false, errorMessage: error };
    }
  }

  async handleNewTodoCommand(command: NewTodoCommand): Promise<CommandStatus> {
    try {
      return await this.client.execute('new-todo-command', command);
    } catch (error) {
      return { success: false, errorMessage: error };
    }
  }

  async handleToggleAllCommand(command: ToggleAllCommand): Promise<CommandStatus> {
    try {
      return this.client.execute('toggle-all-command', command);
    } catch (error) {
      return { success: false, errorMessage: error };
    }
  }

  async handleToggleCommand(command: ToggleCommand): Promise<CommandStatus> {
    try {
      return await this.client.execute('toggle-command', command);
    } catch (error) {
      return { success: false, errorMessage: error };
    }
  }

  async handleTodosQuery(query: TodosQuery): Promise<TodosQueryResult> {
    try {
      return await this.client.execute('todos-query', query);
    } catch (error) {
      return { todos: [] };
    }
  }
}
