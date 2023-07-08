package com.example.project_gunza.common

object FIELD {
    object LOGIN{
        const val ROOT = "logIn"
        const val ID = "id"
    }

    object USER{
        const val ROOT = "user"
        const val ID = "userId"
        const val NAME = "userName"
        const val UNIT = "userUnit"
        const val PROFILE = "userProfile"
        const val MSG = "userMsg"
        const val EXP = "userExp"
        const val POST = "createPost"
        const val CREATE = "createGroup"
        const val VISIT = "recentVisit"
        const val COMMENT = "createComment"
        const val LIKE_POST = "userLikePost"
    }

    object GROUP{
        const val ROOT = "studyGroup"
        const val ID = "groupId"
        const val POST_ID_LIST = "groupPostId"
        const val MEMBER = "groupMemberId"
        const val UNIT = "memberUnit"
    }

    object POST{
        const val ROOT = "post"
        const val ID = "postId"
        const val LIST = "postList"
        const val CREATE_AT = "createdAt"
        const val COMMENT = "comment"
        const val LIKE = "like"
    }

    object COMMENT{
        const val ROOT = "comment"
        const val CREATE_AT = "createdAt"
        const val POST_ID = "postId"
    }

    object TYPE{
        const val NORMAL = "normal"
        const val LIST = "list"

        /** 맵은 사용할 때 대상의 key 값도 입력 해야함
         *- ex) updateFile.key
         * 1. FIELD.USER.VISIT + .${ssgs?.groupId}*/
        const val MAP = "map"
    }
}
