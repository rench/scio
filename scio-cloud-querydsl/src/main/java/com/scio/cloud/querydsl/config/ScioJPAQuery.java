package com.scio.cloud.querydsl.config;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.EntityManager;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAProvider;

public class ScioJPAQuery<T> extends AbstractJPAQuery<T, ScioJPAQuery<T>> {

  /** Creates a new detached query The query can be attached via the clone method */
  public ScioJPAQuery() {
    super(null, JPQLTemplates.DEFAULT, new DefaultQueryMetadata());
  }

  /**
   * Creates a new EntityManager bound query
   *
   * @param em entity manager
   */
  public ScioJPAQuery(EntityManager em) {
    super(em, JPAProvider.getTemplates(em), new DefaultQueryMetadata());
  }

  /**
   * Creates a new EntityManager bound query
   *
   * @param em entity manager
   * @param metadata query metadata
   */
  public ScioJPAQuery(EntityManager em, QueryMetadata metadata) {
    super(em, JPAProvider.getTemplates(em), metadata);
  }

  /**
   * Creates a new query
   *
   * @param em entity manager
   * @param templates templates
   */
  public ScioJPAQuery(EntityManager em, JPQLTemplates templates) {
    super(em, templates, new DefaultQueryMetadata());
  }

  /**
   * Creates a new query
   *
   * @param em entity manager
   * @param templates templates
   * @param metadata query metadata
   */
  public ScioJPAQuery(EntityManager em, JPQLTemplates templates, QueryMetadata metadata) {
    super(em, templates, metadata);
  }

  @Override
  public ScioJPAQuery<T> clone(EntityManager entityManager, JPQLTemplates templates) {
    ScioJPAQuery<T> q = new ScioJPAQuery<T>(entityManager, templates, getMetadata().clone());
    q.clone(this);
    return q;
  }

  @Override
  public ScioJPAQuery<T> clone(EntityManager entityManager) {
    return clone(entityManager, JPAProvider.getTemplates(entityManager));
  }

  @Override
  public <U> ScioJPAQuery<U> select(Expression<U> expr) {
    queryMixin.setProjection(expr);
    @SuppressWarnings("unchecked") // This is the new type
    ScioJPAQuery<U> newType = (ScioJPAQuery<U>) this;
    return newType;
  }

  @Override
  public ScioJPAQuery<Tuple> select(Expression<?>... exprs) {
    queryMixin.setProjection(exprs);
    @SuppressWarnings("unchecked") // This is the new type
    ScioJPAQuery<Tuple> newType = (ScioJPAQuery<Tuple>) this;
    return newType;
  }

  /** */
  private static final long serialVersionUID = -2480778919873514365L;

  /**
   * @param test
   * @param prediecate
   * @return
   */
  public ScioJPAQuery<T> whereIfTrue(boolean test, Predicate prediecate) {
    return test ? where(prediecate) : this;
  }

  /**
   * @param test
   * @param supPrediecate
   * @return
   */
  public ScioJPAQuery<T> whereIfTrue(boolean test, Supplier<Predicate> supPrediecate) {
    return test ? where(supPrediecate.get()) : this;
  }

  /**
   * @param value
   * @param supPrediecate
   * @return
   */
  public ScioJPAQuery<T> whereIfNotNull(T value, Function<T, Predicate> supPrediecate) {
    return value != null ? where(supPrediecate.apply(value)) : this;
  }
  /**
   * @param value
   * @param getPredicate
   * @return
   */
  public <E extends CharSequence> ScioJPAQuery<T> whereIfHasLength(
      E value, Function<E, Predicate> getPredicate) {
    return value != null && value.length() > 0 ? where(getPredicate.apply(value)) : this;
  }

  /**
   * @param value
   * @param getPredicate
   * @return
   */
  public <E extends CharSequence> ScioJPAQuery<T> whereIfHasText(
      E value, Function<E, Predicate> getPredicate) {
    if (value != null && value.length() > 0) {
      for (int i = 0; i < value.length(); ++i) {
        if (Character.isWhitespace(value.charAt(i))) {
          return this;
        }
      }
    }
    return where(getPredicate.apply(value));
  }

  /**
   * @param value
   * @param getPredicate
   * @return
   */
  public <E extends Collection<X>, X> ScioJPAQuery<T> whereIfNotEmpty(
      E value, Function<E, Predicate> getPredicate) {
    return value != null && value.size() > 0 ? where(getPredicate.apply(value)) : this;
  }

  /**
   * @param test
   * @param supPrediecate
   * @return
   */
  public ScioJPAQuery<T> onIfTrue(boolean test, Supplier<Predicate> supPrediecate) {
    return test ? on(supPrediecate.get()) : this;
  }

  /**
   * @param value
   * @param supPrediecate
   * @return
   */
  public ScioJPAQuery<T> onIfNotNull(T value, Function<T, Predicate> supPrediecate) {
    return value != null ? on(supPrediecate.apply(value)) : this;
  }

  /**
   * @param value
   * @param getPredicate
   * @return
   */
  public <E extends CharSequence> ScioJPAQuery<T> onIfHasText(
      E value, Function<E, Predicate> getPredicate) {
    if (value != null && value.length() > 0) {
      for (int i = 0; i < value.length(); ++i) {
        if (Character.isWhitespace(value.charAt(i))) {
          return this;
        }
      }
    }
    return on(getPredicate.apply(value));
  }

  /**
   * @param value
   * @param getPredicate
   * @return
   */
  public <E extends CharSequence> ScioJPAQuery<T> onIfHasLength(
      E value, Function<E, Predicate> getPredicate) {
    return value != null && value.length() > 0 ? on(getPredicate.apply(value)) : this;
  }

  /**
   * @param value
   * @param getPredicate
   * @return
   */
  public <E extends Collection<X>, X> ScioJPAQuery<T> onIfNotEmpty(
      E value, Function<E, Predicate> getPredicate) {
    return value != null && value.size() > 0 ? on(getPredicate.apply(value)) : this;
  }
}
