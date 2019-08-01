package com.scio.cloud.querydsl.config;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAUpdateClause;

public class ScioJPAQueryFactory implements JPQLQueryFactory {
  @Nullable private final JPQLTemplates templates;

  private final Provider<EntityManager> entityManager;

  public ScioJPAQueryFactory(final EntityManager entityManager) {
    this.entityManager =
        new Provider<EntityManager>() {
          @Override
          public EntityManager get() {
            return entityManager;
          }
        };
    this.templates = null;
  }

  public ScioJPAQueryFactory(JPQLTemplates templates, final EntityManager entityManager) {
    this.entityManager =
        new Provider<EntityManager>() {
          @Override
          public EntityManager get() {
            return entityManager;
          }
        };
    this.templates = templates;
  }

  public ScioJPAQueryFactory(Provider<EntityManager> entityManager) {
    this.entityManager = entityManager;
    this.templates = null;
  }

  public ScioJPAQueryFactory(JPQLTemplates templates, Provider<EntityManager> entityManager) {
    this.entityManager = entityManager;
    this.templates = templates;
  }

  @Override
  public JPADeleteClause delete(EntityPath<?> path) {
    if (templates != null) {
      return new JPADeleteClause(entityManager.get(), path, templates);
    } else {
      return new JPADeleteClause(entityManager.get(), path);
    }
  }

  @Override
  public <T> ScioJPAQuery<T> select(Expression<T> expr) {
    return query().select(expr);
  }

  @Override
  public ScioJPAQuery<Tuple> select(Expression<?>... exprs) {
    return query().select(exprs);
  }

  @Override
  public <T> ScioJPAQuery<T> selectDistinct(Expression<T> expr) {
    return select(expr).distinct();
  }

  @Override
  public ScioJPAQuery<Tuple> selectDistinct(Expression<?>... exprs) {
    return select(exprs).distinct();
  }

  @Override
  public ScioJPAQuery<Integer> selectOne() {
    return select(Expressions.ONE);
  }

  @Override
  public ScioJPAQuery<Integer> selectZero() {
    return select(Expressions.ZERO);
  }

  @Override
  public <T> ScioJPAQuery<T> selectFrom(EntityPath<T> from) {
    return select(from).from(from);
  }

  @Override
  public ScioJPAQuery<?> from(EntityPath<?> from) {
    return query().from(from);
  }

  @Override
  public ScioJPAQuery<?> from(EntityPath<?>... from) {
    return query().from(from);
  }

  @Override
  public JPAUpdateClause update(EntityPath<?> path) {
    if (templates != null) {
      return new JPAUpdateClause(entityManager.get(), path, templates);
    } else {
      return new JPAUpdateClause(entityManager.get(), path);
    }
  }

  @Override
  public ScioJPAQuery<?> query() {
    if (templates != null) {
      return new ScioJPAQuery<Void>(entityManager.get(), templates);
    } else {
      return new ScioJPAQuery<Void>(entityManager.get());
    }
  }
}
