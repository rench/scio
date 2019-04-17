package com.scio.cloud.querydsl.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInfo is a Querydsl query type for UserInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserInfo extends EntityPathBase<UserInfo> {

    private static final long serialVersionUID = -538600882L;

    public static final QUserInfo userInfo = new QUserInfo("userInfo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final StringPath realname = createString("realname");

    public final StringPath registerIp = createString("registerIp");

    public final DateTimePath<java.util.Date> registerTime = createDateTime("registerTime", java.util.Date.class);

    public final EnumPath<com.scio.cloud.querydsl.domain.enums.UserStatus> status = createEnum("status", com.scio.cloud.querydsl.domain.enums.UserStatus.class);

    public final StringPath username = createString("username");

    public QUserInfo(String variable) {
        super(UserInfo.class, forVariable(variable));
    }

    public QUserInfo(Path<? extends UserInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfo(PathMetadata metadata) {
        super(UserInfo.class, metadata);
    }

}

