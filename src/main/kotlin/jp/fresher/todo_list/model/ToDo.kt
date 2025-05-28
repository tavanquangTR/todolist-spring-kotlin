package jp.fresher.todo_list.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Todoリスト情報を表すモデル")
@Entity
@Table(name = "todos")
data class ToDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "TodoID"
        ,
        example = "1"
        ,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val id: Int = 0,
    @Schema(
        description = "Todoのタイトル"
        ,
        example = "Learn something"
    )
    var title: String = "",
    @Schema(
        description = "進捗確認"
        ,
        example = "true or false only "

    )
    var isCompleted: Boolean = false
)