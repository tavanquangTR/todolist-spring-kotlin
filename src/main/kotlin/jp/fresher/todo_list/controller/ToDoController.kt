package jp.fresher.todo_list.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jp.fresher.todo_list.model.ToDo
import jp.fresher.todo_list.repository.ToDoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException




@RestController
@RequestMapping("/api/todos")
@Tag(name = "TodoAPI"
    ,
    description = "Todos詳細API")
class ToDoController(private val toDoRepository: ToDoRepository) {

    // TODO一覧の取得
    @CrossOrigin(origins = ["http://127.0.0.1:5500"])
    @GetMapping
    @Operation(summary = "TodoIDで取得")
    @ApiResponse(
        responseCode = "200"

        ,
        description = "成功"
        ,
        content = [Content(
            schema =
                Schema(implementation = ToDo::class)
        )]
    )
    fun getAllTodos(): List<ToDo> = toDoRepository.findAll()



    // 指定したTODOを表示する
    @Operation(summary = "TodoIDで取得")
    @ApiResponse(
        responseCode = "200"
        ,
        description = "取得成功"
        ,
        content = [Content(
            schema =
                Schema(implementation = ToDo::class)
        )]
    )
    @GetMapping("/{id}")

    fun getTodoById(@PathVariable id: Int): ToDo {
        return toDoRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id $id") }
    }

    // 新規登録
    @CrossOrigin(origins = ["http://127.0.0.1:5500"])
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Todo作成")
    @ApiResponse(
        responseCode = "201"
        ,
        description = "作成成功"
        ,
        content = [Content(
            schema =
                Schema(implementation = ToDo::class)
        )]
    )
    fun createTodo(@RequestBody newTodo: ToDo): ToDo {
        return toDoRepository.save(newTodo.copy(id = 0))
    }

    // 更新処理
    @Operation(summary = "TodoIDで更新")
    @ApiResponse(
        responseCode = "201"
        ,
        description = "成功"
        ,
        content = [Content(
            schema =
                Schema(implementation = ToDo::class)
        )]
    )
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Int,
        @RequestBody updatedTodo: ToDo
    ): ResponseEntity<ToDo> {
        return toDoRepository.findById(id).map { existingTodo ->
                val savedTodo = toDoRepository.save(
                    existingTodo.copy(
                        title = updatedTodo.title ?: existingTodo.title,
                        isCompleted = updatedTodo.isCompleted
                    )
                )
                ResponseEntity.ok(savedTodo)
            }
            .orElse(ResponseEntity.notFound().build())
    }

    // 削除処理
    @Operation(summary = "TodoIDで削除")
    @ApiResponse(
        responseCode = "204"
        ,
        description = "削除成功"

    )
    @DeleteMapping("/{id}")
    fun deleteTodoById(@PathVariable id: Int): ResponseEntity<Void> {
        return toDoRepository.findById(id).map { todo ->
            toDoRepository.delete(todo)
            ResponseEntity.noContent().build<Void>()
        }.orElse(ResponseEntity.notFound().build())
    }
}