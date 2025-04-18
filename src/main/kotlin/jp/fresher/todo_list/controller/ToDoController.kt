package jp.fresher.todo_list.controller

import jp.fresher.todo_list.model.ToDo
import jp.fresher.todo_list.repository.ToDoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/todos")
class ToDoController(private val toDoRepository: ToDoRepository) {

    // TODO一覧の取得
    @GetMapping
    fun getAllTodos(): List<ToDo> = toDoRepository.findAll()

    // 指定したTODOを表示する
    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Int): ToDo {
        return toDoRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id $id") }
    }

    // 新規登録
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@RequestBody newTodo: ToDo): ToDo {
        return toDoRepository.save(newTodo.copy(id = 0))
    }

    // 更新処理
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Int,
        @RequestBody updatedTodo: ToDo
    ): ResponseEntity<ToDo> {
        return toDoRepository.findById(id).map { existingTodo ->
                val savedTodo = toDoRepository.save(
                    existingTodo.copy(
                        title = updatedTodo.title,
                        isCompleted = updatedTodo.isCompleted
                    )
                )
                ResponseEntity.ok(savedTodo)
            }
            .orElse(ResponseEntity.notFound().build())
    }

    // 削除処理
    @DeleteMapping("/{id}")
    fun deleteTodoById(@PathVariable id: Int): ResponseEntity<Void> {
        return toDoRepository.findById(id).map { todo ->
            toDoRepository.delete(todo)
            ResponseEntity.noContent().build<Void>()
        }.orElse(ResponseEntity.notFound().build())
    }
}