package com.example.project_gunza.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_gunza.TestViewModel
import com.example.project_gunza.main_page.UserViewModel
import com.example.project_gunza.post_viewer.PostViewModel
import com.example.project_gunza.study_group.StudyGroupViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val param: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TestViewModel::class.java) -> { TestViewModel(param) as T }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> { UserViewModel(param) as T }
            modelClass.isAssignableFrom(StudyGroupViewModel::class.java) -> { StudyGroupViewModel(param) as T }
            modelClass.isAssignableFrom(PostViewModel::class.java) -> { PostViewModel(param) as T }
            else -> { throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}") }
        }
    }
}