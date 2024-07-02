/*
 * This file is generated by jOOQ.
 */
package com.github.propan.teabuddy.repository.jooq.tables;


import com.github.propan.teabuddy.models.ItemType;
import com.github.propan.teabuddy.models.Store;
import com.github.propan.teabuddy.repository.jooq.Keys;
import com.github.propan.teabuddy.repository.jooq.Public;
import com.github.propan.teabuddy.repository.jooq.tables.records.ItemsRecord;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.EnumConverter;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Items extends TableImpl<ItemsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>PUBLIC.ITEMS</code>
     */
    public static final Items ITEMS = new Items();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ItemsRecord> getRecordType() {
        return ItemsRecord.class;
    }

    /**
     * The column <code>PUBLIC.ITEMS.ID</code>.
     */
    public final TableField<ItemsRecord, UUID> ID = createField(DSL.name("ID"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ITEMS.STORE</code>.
     */
    public final TableField<ItemsRecord, Store> STORE = createField(DSL.name("STORE"), SQLDataType.INTEGER.nullable(false), this, "", new EnumConverter<Integer, Store>(Integer.class, Store.class));

    /**
     * The column <code>PUBLIC.ITEMS.VENDOR</code>.
     */
    public final TableField<ItemsRecord, String> VENDOR = createField(DSL.name("VENDOR"), SQLDataType.VARCHAR(1000000000).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ITEMS.TITLE</code>.
     */
    public final TableField<ItemsRecord, String> TITLE = createField(DSL.name("TITLE"), SQLDataType.VARCHAR(1000000000).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ITEMS.TYPE</code>.
     */
    public final TableField<ItemsRecord, ItemType> TYPE = createField(DSL.name("TYPE"), SQLDataType.INTEGER.nullable(false), this, "", new EnumConverter<Integer, ItemType>(Integer.class, ItemType.class));

    /**
     * The column <code>PUBLIC.ITEMS.SOURCE_URL</code>.
     */
    public final TableField<ItemsRecord, String> SOURCE_URL = createField(DSL.name("SOURCE_URL"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>PUBLIC.ITEMS.IMAGE_URL</code>.
     */
    public final TableField<ItemsRecord, String> IMAGE_URL = createField(DSL.name("IMAGE_URL"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>PUBLIC.ITEMS.PRICE</code>.
     */
    public final TableField<ItemsRecord, String> PRICE = createField(DSL.name("PRICE"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>PUBLIC.ITEMS.CREATED_AT</code>.
     */
    public final TableField<ItemsRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "");

    private Items(Name alias, Table<ItemsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Items(Name alias, Table<ItemsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>PUBLIC.ITEMS</code> table reference
     */
    public Items(String alias) {
        this(DSL.name(alias), ITEMS);
    }

    /**
     * Create an aliased <code>PUBLIC.ITEMS</code> table reference
     */
    public Items(Name alias) {
        this(alias, ITEMS);
    }

    /**
     * Create a <code>PUBLIC.ITEMS</code> table reference
     */
    public Items() {
        this(DSL.name("ITEMS"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<ItemsRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_4;
    }

    @Override
    public Items as(String alias) {
        return new Items(DSL.name(alias), this);
    }

    @Override
    public Items as(Name alias) {
        return new Items(alias, this);
    }

    @Override
    public Items as(Table<?> alias) {
        return new Items(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Items rename(String name) {
        return new Items(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Items rename(Name name) {
        return new Items(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Items rename(Table<?> name) {
        return new Items(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Items where(Condition condition) {
        return new Items(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Items where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Items where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Items where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Items where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Items where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Items where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Items where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Items whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Items whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
