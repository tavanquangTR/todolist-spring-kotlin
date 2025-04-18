package jp.fresher.todo_list.repository

import jp.fresher.todo_list.model.ToDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToDoRepository : JpaRepository<ToDo,Int> {
}