package icu.cucurbit.sql.filter;

import com.google.common.collect.Sets;
import icu.cucurbit.bo.FilterTable;
import icu.cucurbit.sql.TableRule;

import java.util.*;
import java.util.function.Supplier;

public class LikeFilter extends RuleFilter {

    public static final Set<String> LIKE_RELATIONS = Sets.newHashSet("LIKE", "NOT LIKE");

    private final String field;
    private final String relation;
    private final List<Object> jdbcParameters;

    public LikeFilter(TableRule rule) {
        Objects.requireNonNull(rule);
        Objects.requireNonNull(rule.getTableName());
        Objects.requireNonNull(rule.getField());
        Objects.requireNonNull(rule.getRelation());
        Objects.requireNonNull(rule.getTarget());

        this.filterTable = new FilterTable(rule.getTableName(), rule.getTableName());
        this.field = rule.getField();
        String relation = rule.getRelation().trim().toUpperCase();
        if (!LIKE_RELATIONS.contains(relation)) {
            throw new IllegalArgumentException("cannot create LikeFilter, relation [" + relation + "] not support.");
        }
        this.relation = relation;
        Object target = rule.getTarget();
        if (!(target instanceof String)) {
            throw new IllegalArgumentException("LikeFilter require a String value, but got ["
                    + target.getClass().getName() + "].");
        }
        this.jdbcParameters = new ArrayList<>();
        jdbcParameters.add(target);
    }

    @Override
    public String toSqlSnippet(FilterTable filterTable, Supplier<String> placeHolderSupplier) {
        Objects.requireNonNull(placeHolderSupplier);

        filterTable = Optional.ofNullable(filterTable).orElse(this.filterTable);
        String tableName = Optional.ofNullable(filterTable.getAlias()).orElse(filterTable.getName());
        return tableName + "." + field + " " + relation + " " + placeHolderSupplier.get();
    }

    @Override
    public List<Object> getJdbcParameters() {
        return jdbcParameters;
    }
}
