package com.example.project_gunza.study_group

import android.util.Log
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.data_class.SimpleUserStruct
import com.example.project_gunza.data_class.PostStruct
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
/** 스터디 그룹 모델*/
class StudyGroupRepository {
    private val postDB = Firebase.firestore.collection(FIELD.POST.ROOT)
    private val userDB = Firebase.firestore.collection(FIELD.USER.ROOT)
    private val groupDB = Firebase.firestore.collection(FIELD.GROUP.ROOT)

    var groupPostInfo = listOf<PostStruct>()
    var authorInfo = listOf<SimpleUserStruct>()

    /** @param groupId 해당하는 그룹의 게시 글을 가져옴*/
    suspend fun fetchData(groupId: String){
        Log.d("LOG_CHECK", "StudyGroupRepository :: fetchData() -> fetch group($groupId) data")

        val value = postDB
            .whereEqualTo(FIELD.GROUP.ID, groupId)
            .orderBy(FIELD.POST.CREATE_AT, Query.Direction.DESCENDING)
            .get()
            .await()

        withContext(Dispatchers.IO) {
            val originList = value.toList()
            val tempGroupList = MutableList(originList.size) { PostStruct() }
            val tempAuthorList = MutableList(originList.size) { SimpleUserStruct() }

            if(tempGroupList.size > 0){
                originList.mapIndexed { index, snapShot ->
                    async {
                        val postStruct = snapShot.toObject(PostStruct::class.java)
                        tempGroupList[index] = postStruct
                        tempAuthorList[index] = getAuthor(postStruct.authorId)
                    }
                }.awaitAll()
                authorInfo = tempAuthorList
                groupPostInfo = tempGroupList
            }
        }
    }

    /** 유저가 그룹 내에서 작성한 게시글만 보여줌
     *- 작성한 게시글이 없는 경우 null 리턴
     * @return pair<작성글, 작성자 정보> */
    suspend fun viewMyPost(authorId: String): Pair<List<PostStruct>, List<SimpleUserStruct>>?{
        val authorList: List<SimpleUserStruct>?
        val postList = groupPostInfo.filter { post ->
            post.authorId == authorId
        }

        return if (postList.isNotEmpty()){
            val author = getAuthor(authorId)
            authorList = List(postList.size) { author }
            Pair(postList, authorList)
        }
        else{ null }
    }

    /** 게시글 제목에 [searchTitle] 이 포함된 게시글을 리턴
     *- 조건을 만족하는 게시글이 없는 경우 null 리턴
     * @return pair<게시글, 작성자 정보> */
    suspend fun searchPostByTitle(searchTitle: String): Pair<List<PostStruct>, List<SimpleUserStruct>>?{
        val postList = groupPostInfo.filter { post->
            post.postTitle.contains(searchTitle)
        }

        postList.ifEmpty { return null }

        val authorList = mutableListOf<SimpleUserStruct>()
        postList.forEach { post->
            val author = getAuthor(post.authorId)
            authorList.add(author)
        }
        return Pair(postList, authorList)
    }

    private suspend fun getAuthor(authorId: String) =
        userDB.document(authorId)
            .get()
            .await()
            .toObject(SimpleUserStruct::class.java)?: SimpleUserStruct()


    /** groupDB 데이터 업데이트
     * @param fieldType 업데이트 대상 유형 : 일반, 리스트, 맵
     * @param key 필드 타입이 [FIELD.TYPE.MAP] 경우 맵의 키값*/
    fun updateGroupInfo(groupId: String, field: String, value: Any, fieldType: String, keyValue: String? = null){
        groupDB.document(groupId)
            .run{
                when(fieldType){
                    FIELD.TYPE.NORMAL -> { this.update(field, FieldValue.increment(value as Long)) }
                    FIELD.TYPE.LIST -> { this.update(field, FieldValue.arrayUnion(value)) }
                    FIELD.TYPE.MAP -> {
                        keyValue?.let { key ->
                            this.update("$field.$key", FieldValue.increment(value as Long))
                        }?: run{
                            Log.e("LOG_CHECK", "StudyGroupRepository :: updateGroupInfo() -> key is null!!!")
                        }
                    }
                    else -> {}
                }
            }
    }

    fun createGroupPost(post: PostStruct, callback: ()->Unit){
        postDB
            .document(post.postId)
            .set(post)
            .addOnSuccessListener {
                callback()
            }
    }
}