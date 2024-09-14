package com.example.InstaPrj.entity;

public enum ClubMemberRole {
    // 권한은 USER, MANAGER, ADMIN으로 나뉘지만 이 모두는 Member 엔티티에 속함.
//  USER는 일반 사용자로서 제한된 기능만 사용 가능하며,
//  MANAGER는 특정 그룹이나 프로젝트를 관리할 수 있는 권한이 있고,
//  ADMIN은 시스템 전반에 대한 모든 권한을 가지고 있는 최고 관리자의 역할
    USER, MANAGER, ADMIN
}
