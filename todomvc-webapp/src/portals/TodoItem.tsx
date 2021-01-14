import React, { useEffect, useRef, useState } from 'react';
import classnames from 'classnames';

import { Todo } from '../domain/data';
import { usePrevious } from './hooks';

export type TodoItemProps = Readonly<{
  todo: Todo;
  editing?: boolean;
  onToggle?: () => void;
  onEdit?: () => void;
  onSave?: (title: string) => void;
  onCancel?: () => void;
  onDestroy?: () => void;
}>;

function TodoItem({ todo, editing = false, onToggle, onEdit, onSave, onCancel, onDestroy }: TodoItemProps) {
  const previousEditing = usePrevious(editing);
  const [editText, setEditText] = useState(todo.title);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const edit = inputRef.current;
    if (!previousEditing && editing && edit != null) {
      edit.focus();
      edit.setSelectionRange(edit.value.length, edit.value.length);
    }
  });

  function handleEdit() {
    setEditText(todo.title);
    onEdit?.();
  }

  function handleSubmit() {
    var val = editText.trim();
    if (val) {
      setEditText(val);
      onSave?.(val);
    } else {
      onDestroy?.();
    }
  }

  function handleChange() {
    if (inputRef.current == null) {
      return;
    }

    setEditText(inputRef.current.value);
  }

  function handleKeyDown(event: React.KeyboardEvent<HTMLInputElement>) {
    if (event.key === 'Escape') {
      setEditText(todo.title);
      onCancel?.();
    } else if (event.key === 'Enter') {
      handleSubmit();
    }
  }

  return (
    <li className={classnames({ completed: todo.completed, editing })}>
      <div className="view">
        <input title="Toggle" className="toggle" type="checkbox" checked={todo.completed} onChange={onToggle} />
        <label onDoubleClick={handleEdit}>{todo.title}</label>
        <button title="Destroy" className="destroy" onClick={onDestroy}></button>
      </div>
      <input
        ref={inputRef}
        className="edit"
        value={editText}
        onBlur={handleSubmit}
        onChange={handleChange}
        onKeyDown={handleKeyDown}
      />
    </li>
  );
}

export default TodoItem;
