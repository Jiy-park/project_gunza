package com.dd2d.gunza.common

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
        const val TITLE = "postTitle"
        const val CONTENT = "content"
    }

    object COMMENT{
        const val ROOT = "comment"
        const val CREATE_AT = "createdAt"
        const val POST_ID = "postId"
        const val CONTENT = "commentBody"
    }

    object TYPE{
        const val NORMAL = "normal"
        const val LIST = "list"

        /** 맵은 사용할 때 대상의 key 값도 입력 해야함
         *- ex) updateFile.key
         * 1. FIELD.USER.VISIT + .${ssgs?.groupId}*/
        const val MAP = "map"
    }

    object JSON{
        /** 대학교 이름 = "1"*/
        const val UNIVERSITY_NAME = "1"

        /** 과목명 = "2"*/
        const val SUBJECT_NAME = "2"

        /** 학점 = "3"*/
        const val CREDIT = "3"
    }
}
