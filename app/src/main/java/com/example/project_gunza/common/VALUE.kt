package com.example.project_gunza.common

object VALUE {
    /** 그리드 배치에서 한 줄에 배치할 뷰의 개수 = 3*/
    const val NUM_OF_VIEW_GRID = 3

    /** 게시글 하나 생성 시 경험치 = 1L */
    const val EXP_OF_ONE_POST = 1L

    /** 그룹 하나 생성 시 경험치 = 2L*/
    const val EXP_OF_ONE_GROUP = 2L

    /** 경험치 -> 레벨 변환 상수 = 5
     *- 변환 공식 : 레벨 = 경험치 / 변환 상수*/
    const val LEVEL_CONVERT_VAL = 5


    /** 유저 한명이 만들 수 있는 그룹 수 = 3*/
    const val CREATE_GROUP_LIMIT = 3

    /** 유저가 최근 방문한 그룹 기준 정렬 = 0*/
    const val SORT_BY_RECENT_VISIT = 0

    /** 그룹의 인원 기준 정렬 = 1*/
    const val SORT_BY_NUM_OF_GROUP_MEMBER = 1

    /** 정렬 기준이 선택되지 않았음 = -1*/
    const val NOT_SELECTED_SORT = -1

    /** 스터디 그룹 검색 = "검색할 스터디 그룹 이름"*/
    const val SEARCH_STUDY_GROUP = "검색할 스터디 그룹 이름"

    /** 그룹 게시글 검색 = "검색할 게시글 제목"*/
    const val SEARCH_GROUP_POST = "검색할 게시글 제목"
}