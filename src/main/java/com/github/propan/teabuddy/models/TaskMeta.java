package com.github.propan.teabuddy.models;

import com.github.propan.teabuddy.parsers.StoreParser;

import java.util.UUID;

public record TaskMeta(UUID crawlerId, StoreParser parser) { }
