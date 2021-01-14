import React from 'react';
import { Meta, Story } from '@storybook/react';

import TodoItem, { TodoItemProps } from './TodoItem';

export default {
  title: 'Todo Item',
  component: TodoItem,
  decorators: [
    (story) => (
      <section className="todoapp">
        <section className="main">
          <ul className="todo-list">{story()}</ul>
        </section>
      </section>
    ),
  ],
} as Meta;

const Template: Story<TodoItemProps> = (args) => <TodoItem {...args} />;

export const Default = Template.bind({});
Default.args = {
  todo: { id: '1', title: 'Taste JavaScript', completed: false },
  editing: false,
};

export const Completed = Template.bind({});
Completed.args = {
  ...Default.args,
  todo: { id: '1', title: 'Taste JavaScript', completed: true },
};

export const Editing = Template.bind({});
Editing.args = {
  ...Default.args,
  editing: true,
};
