import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { Todo } from '../domain/data';
import TodoItem from './TodoItem';

const todo: Todo = { id: '1', title: 'Taste JavaScript', completed: false };

it('Toggle', () => {
  const handleToggle = jest.fn();
  render(<TodoItem todo={todo} onToggle={handleToggle} />);
  const input = screen.getByTitle('Toggle') as HTMLInputElement;

  fireEvent.click(input);

  expect(handleToggle).toBeCalledTimes(1);
});

it('Edit', async () => {
  const handleToggle = jest.fn();
  const handleEdit = jest.fn();
  const { rerender } = render(<TodoItem todo={todo} onToggle={handleToggle} onEdit={handleEdit} />);
  rerender(<TodoItem todo={{ ...todo, title: 'Buy a unicorn' }} onToggle={handleToggle} onEdit={handleEdit} />);
  const label = screen.getByText('Buy a unicorn') as HTMLLabelElement;
  const input = screen.getByDisplayValue('Taste JavaScript') as HTMLInputElement;

  fireEvent.doubleClick(label);

  expect(input.value).toEqual('Buy a unicorn');
  expect(handleEdit).toBeCalledTimes(1);
});

describe('Editing', () => {
  it('Save', () => {
    const handleToggle = jest.fn();
    const handleSave = jest.fn();
    const handleCancel = jest.fn();
    const handleDestroy = jest.fn();
    render(
      <TodoItem
        todo={todo}
        editing={true}
        onToggle={handleToggle}
        onSave={handleSave}
        onCancel={handleCancel}
        onDestroy={handleDestroy}
      />
    );
    const label = screen.getByText('Taste JavaScript') as HTMLLabelElement;
    fireEvent.doubleClick(label);
    const input = screen.getByDisplayValue('Taste JavaScript') as HTMLInputElement;

    userEvent.type(input, '{selectall}Buy a unicorn');
    fireEvent.keyDown(input, { key: 'Enter' });

    expect(handleSave).toBeCalledTimes(1);
    expect(handleSave).toBeCalledWith('Buy a unicorn');
    expect(handleCancel).not.toBeCalled();
    expect(handleDestroy).not.toBeCalled();
  });

  it('Cancel', () => {
    const handleToggle = jest.fn();
    const handleSave = jest.fn();
    const handleCancel = jest.fn();
    const handleDestroy = jest.fn();
    render(
      <TodoItem
        todo={todo}
        editing={true}
        onToggle={handleToggle}
        onSave={handleSave}
        onCancel={handleCancel}
        onDestroy={handleDestroy}
      />
    );
    const label = screen.getByText('Taste JavaScript') as HTMLLabelElement;
    fireEvent.doubleClick(label);
    const input = screen.getByDisplayValue('Taste JavaScript') as HTMLInputElement;

    userEvent.type(input, '{selectall}Buy a unicorn');
    fireEvent.keyDown(input, { key: 'Escape' });

    expect(handleSave).not.toBeCalled();
    expect(handleCancel).toBeCalledTimes(1);
    expect(handleDestroy).not.toBeCalled();
  });

  it('Destroy', () => {
    const handleToggle = jest.fn();
    const handleSave = jest.fn();
    const handleCancel = jest.fn();
    const handleDestroy = jest.fn();
    render(
      <TodoItem
        todo={todo}
        editing={true}
        onToggle={handleToggle}
        onSave={handleSave}
        onCancel={handleCancel}
        onDestroy={handleDestroy}
      />
    );
    const label = screen.getByText('Taste JavaScript') as HTMLLabelElement;
    fireEvent.doubleClick(label);
    const input = screen.getByDisplayValue('Taste JavaScript') as HTMLInputElement;

    userEvent.type(input, '{selectall}{del}');
    fireEvent.keyDown(input, { key: 'Enter' });

    expect(handleSave).not.toBeCalled();
    expect(handleCancel).not.toBeCalled();
    expect(handleDestroy).toBeCalledTimes(1);
  });
});

it('Destroy', () => {
  const handleToggle = jest.fn();
  const handleDestroy = jest.fn();
  render(<TodoItem todo={todo} onToggle={handleToggle} onDestroy={handleDestroy} />);
  const input = screen.getByTitle('Destroy') as HTMLInputElement;

  fireEvent.click(input);

  expect(handleDestroy).toBeCalledTimes(1);
});
