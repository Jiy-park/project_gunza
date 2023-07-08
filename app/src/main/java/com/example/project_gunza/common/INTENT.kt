package com.example.project_gunza.common

object INTENT {
    object SIGN{
        const val ID = "id"
        const val PW = "pw"
        const val OUT = "signOut"
    }

    object GROUP{
        /** = "simpleStudyGroupStruct"*/
        const val SSGS = "simpleStudyGroupStruct"

        /** * = "groupRankTop3"*/
        const val RANK = "groupRankTop3"

        /** * = "groupId"*/
        const val ID = "groupId"
    }

    object POST{
        /** = "createPost" */
        const val CREATE_POST = "createPost"

        /** = "postStruct"*/
        const val POST = "postStruct"

        /** = "postAuthor"*/
        const val AUTHOR = "postAuthor"
    }

    object CERTIFICATE{
        /** = "certificateOrder"*/
        const val ORDER = "certificateOrder"

        /** = "exam"*/
        const val EXAM = "exam"

        /** = "pass"*/
        const val PASS = "pass"
    }
}