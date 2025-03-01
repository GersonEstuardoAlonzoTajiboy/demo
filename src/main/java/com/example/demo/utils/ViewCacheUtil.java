package com.example.demo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper for loading and caching FXML views.
 */
public class ViewCacheUtil {

    private static final Map<String, Node> cache = new ConcurrentHashMap<>();

    private ViewCacheUtil() {
        throw new UnsupportedOperationException("Utility class. Do not instantiate!");
    }

    /**
     * Try to get the cached view.
     *
     * @param fxmlPath Path of the FXML.
     * @return The view exists in cache, otherwise null.
     */
    public static Node getView(String fxmlPath) {
        return cache.get(fxmlPath);
    }

    /**
     * Loads the FXML, caches it, and returns it.
     *
     * @param fxmlPath FXML Route.
     * @return The loaded view.
     * @throws IOException If an error occurs during loading.
     */
    public static Node loadView(String fxmlPath) throws IOException {
        Node view = FXMLLoader.load(Objects.requireNonNull(ViewCacheUtil.class.getResource(fxmlPath)));
        cache.put(fxmlPath, view);
        return view;
    }
}
