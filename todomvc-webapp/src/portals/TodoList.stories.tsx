import React from 'react';
import { Meta, Story } from '@storybook/react';

import TodoList, { TodoListProps } from './TodoList';
import TodoItem from './TodoItem';

export default {
  title: 'Todo List',
  component: TodoList,
  subcomponents: { TodoItem },
  decorators: [(story) => <section className="todoapp">{story()}</section>],
} as Meta;

const Template: Story<TodoListProps> = (args) => <TodoList {...args} />;

export const Default = Template.bind({});
Default.args = {
  allCompleted: false,
  children: [
    <TodoItem key="1" todo={{ id: '1', title: 'Taste JavaScript', completed: true }} />,
    <TodoItem key="2" todo={{ id: '2', title: 'Buy a unicorn', completed: false }} />,
  ],
};

export const AllCompleted = Template.bind({});
AllCompleted.args = {
  ...Default.args,
  allCompleted: true,
};
