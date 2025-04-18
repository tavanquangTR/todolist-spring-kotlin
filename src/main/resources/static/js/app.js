document.addEventListener('DOMContentLoaded', () => {
    // DOM要素
    const newTodoInput = document.getElementById('new-todo');
    const addTodoButton = document.getElementById('add-todo');
    const todoList = document.getElementById('todo-list');
    const filterButtons = document.querySelectorAll('.filter-btn');
    
    let currentFilter = 'all';
    let todos = [];
    
    // 初期化処理 - すべてのTODOを取得
    fetchTodos();
    
    // イベントリスナー
    addTodoButton.addEventListener('click', addTodo);
    newTodoInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            addTodo();
        }
    });
    
    filterButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            currentFilter = btn.getAttribute('data-filter');
            filterButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            renderTodos();
        });
    });
    
    // APIからTODOを取得
    async function fetchTodos() {
        try {
            const response = await fetch('/api/todos');
            if (!response.ok) {
                throw new Error('TODOの取得に失敗しました');
            }
            todos = await response.json();
            renderTodos();
        } catch (error) {
            console.error('エラー:', error);
            alert('TODOの取得に失敗しました: ' + error.message);
        }
    }
    
    // 新しいTODOを追加
    async function addTodo() {
        const title = newTodoInput.value.trim();
        if (!title) return;
        
        try {
            const response = await fetch('/api/todos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    title: title,
                    isCompleted: false
                })
            });
            
            if (!response.ok) {
                throw new Error('TODOの追加に失敗しました');
            }
            
            const newTodo = await response.json();
            todos.push(newTodo);
            renderTodos();
            newTodoInput.value = '';
        } catch (error) {
            console.error('エラー:', error);
            alert('TODOの追加に失敗しました: ' + error.message);
        }
    }
    
    // TODOの状態を更新
    async function updateTodoStatus(id, isCompleted) {
        try {
            const todoToUpdate = todos.find(todo => todo.id === id);
            
            const response = await fetch(`/api/todos/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    title: todoToUpdate.title,
                    isCompleted: isCompleted
                })
            });
            
            if (!response.ok) {
                throw new Error('TODOの更新に失敗しました');
            }
            
            const updatedTodo = await response.json();
            todos = todos.map(todo => todo.id === id ? updatedTodo : todo);
            renderTodos();
        } catch (error) {
            console.error('エラー:', error);
            alert('TODOの更新に失敗しました: ' + error.message);
        }
    }
    
    // TODOを削除
    async function deleteTodo(id) {
        try {
            const response = await fetch(`/api/todos/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                throw new Error('TODOの削除に失敗しました');
            }
            
            todos = todos.filter(todo => todo.id !== id);
            renderTodos();
        } catch (error) {
            console.error('エラー:', error);
            alert('TODOの削除に失敗しました: ' + error.message);
        }
    }
    
    // フィルタリングされたTODOのリストをレンダリング
    function renderTodos() {
        let filteredTodos = todos;
        
        if (currentFilter === 'active') {
            filteredTodos = todos.filter(todo => !todo.isCompleted);
        } else if (currentFilter === 'completed') {
            filteredTodos = todos.filter(todo => todo.isCompleted);
        }
        
        todoList.innerHTML = '';
        
        filteredTodos.forEach(todo => {
            const li = document.createElement('li');
            li.className = `todo-item ${todo.isCompleted ? 'completed' : ''}`;
            
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'todo-checkbox';
            checkbox.checked = todo.isCompleted;
            checkbox.addEventListener('change', () => {
                updateTodoStatus(todo.id, checkbox.checked);
            });
            
            const todoText = document.createElement('span');
            todoText.className = 'todo-text';
            todoText.textContent = todo.title;
            
            const deleteButton = document.createElement('button');
            deleteButton.className = 'delete-todo';
            deleteButton.textContent = '削除';
            deleteButton.addEventListener('click', () => {
                if (confirm(`"${todo.title}" を削除しますか？`)) {
                    deleteTodo(todo.id);
                }
            });
            
            li.appendChild(checkbox);
            li.appendChild(todoText);
            li.appendChild(deleteButton);
            todoList.appendChild(li);
        });
    }
});