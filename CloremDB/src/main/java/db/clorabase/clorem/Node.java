package db.clorabase.clorem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import port.org.json.JSONArray;
import port.org.json.JSONException;
import port.org.json.JSONObject;

/**
 * This class represent a node in the database. A node is like a folder which may contain other folders or files. You can perform all the CRUD
 * operations on a node using this class.
 *
 * @see <a href="https://github.com/ErrorxCode/CloremDB">API Reference and Guide</a> for more information
 */
public class Node {
    protected JSONObject root;
    protected JSONObject object;
    protected String path = "";

    protected Node(Map<String, ?> map) {
        object = new JSONObject(map);
        root = object;
    }


    protected Node(JSONObject object, JSONObject root, String path) {
        this.object = object;
        this.root = root;
        this.path = path;
    }

    /**
     * Enters into the node present in the current node if exist, creates otherwise.
     *
     * @param name The name or relative path of the node.
     * @return The new node.
     */
    public Node node(@NonNull String name) {
        try {
            if (name.contains("/")) {
                String path = "";

                if (name.startsWith("/"))
                    name = name.substring(1);

                String[] nodes = name.split("/");
                JSONObject newObject = object;
                for (String node : nodes) {
                    if (newObject.optJSONObject(node) == null) {
                        JSONObject jsonObject = new JSONObject();
                        newObject.put(node, jsonObject);
                        newObject = jsonObject;
                    } else
                        newObject = newObject.optJSONObject(node);

                    path += "/" + node;
                }
                return new Node(newObject, root, path);
            } else {
                JSONObject jsonObject = object.optJSONObject(name);
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                    object.put(name, jsonObject);
                }
                path += "/" + name;
                return new Node(jsonObject, root, path);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Cannot create node for some reason.", Reasons.REASONS_NODE_CREATION_ERROR);
        }
    }

    /**
     * Checks if this child or node is in current node or not
     * @param key The key of the child or node
     * @return True if yse, false otherwise
     */
    public boolean hasChild(@NonNull String key){
        return object.has(key);
    }

    /**
     * Deletes the current node including its children. You don't need to call commit() after calling this.
     * Any operation performed on the node after calling this method will result in {@code NullPointerException}
     * as this node becomes null when deleted.
     *
     * @return true if the node is deleted, false when there is no such node.
     */
    public boolean delete(@NonNull String name) {
        boolean result = object.has(name);
        object.remove(name);
        commit();
        return result;
    }

    /**
     * Inserts a POJO object creating a new node with name as 'key'. This object will not be
     * listed or considered a child of current node.
     *
     * @param key    The key of the object.
     * @param object The object to be inserted.
     * @return The old node.
     */
    public Node put(@NonNull String key, @NonNull Object object) {
        try {
            this.object.put(key, new JSONObject(new Gson().toJson(object)));
        } catch (JSONException e) {
            throw new CloremDatabaseException("Cannot deserialize object. Make sure it's a POJO", Reasons.REASONS_INVALID_TYPE);
        }
        return this;
    }

    /**
     * Inserts a non-empty list into the database. The list can be of any generic type.
     *
     * @param key  The key of the list.
     * @param list The list to be inserted.
     * @return This node.
     */
    public Node put(@NonNull String key, @NonNull List list) {
        try {
            this.object.put(key, new JSONArray(new Gson().toJson(list)));
        } catch (JSONException e) {
            throw new CloremDatabaseException("Cannot insert list into the database, Make sure that the item of the list are POJO", Reasons.REASONS_INVALID_TYPE);
        } catch (IndexOutOfBoundsException e) {
            object.put(key, new JSONArray());
        }
        return this;
    }

    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     *
     * @param key   The key to access the value
     * @param value The string value to put. null to remove existing mapping.
     * @return The current node
     */
    public Node put(@NonNull String key, @Nullable String value) {
        try {
            object.put(key, value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(), Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     *
     * @param key   The key to access the value
     * @param value The long or int value to put.
     * @return The current node
     */
    public Node put(@NonNull String key, long value) {
        try {
            object.put(key, value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(), Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     *
     * @param key   The key to access the value
     * @param value The boolean value to put.
     * @return The current node
     */
    public Node put(@NonNull String key, boolean value) {
        try {
            object.put(key, value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(), Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Creates a new key-value pair in the current node. Updates if already exist.
     *
     * @param key   The key to access the value
     * @param value The double value to put.
     * @return The current node
     */
    public Node put(@NonNull String key, double value) {
        try {
            object.put(key, value);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Something horribly gone wrong. This error should not occur is most of the cases, Please create a issue on github regarding this error\n\n---[Stack trace]---\n" + e.getLocalizedMessage(), Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Puts all the data from the map to the current node.
     *
     * @param data The map to put.
     * @return The current node
     */
    public Node put(@NonNull Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            object.put(key, value);
        }
        return this;
    }

    /**
     * Gets the string from the current node in the database.
     *
     * @param key The key of the value
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public String getString(@NonNull String key, String defaultValue) {
        return object.optString(key).isEmpty() ? defaultValue : object.optString(key);
    }

    /**
     * Gets the number from the database. This number could either be an int or a long depending on what did you put.
     *
     * @param key The key of the value
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public long getNumber(@NonNull String key, long defaultValue) {
        return object.optInt(key) == 0 ? defaultValue : object.optInt(key);
    }

    /**
     * Gets the boolean from the current node in the database.
     *
     * @param key The key of the value
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        try {
            return object.getBoolean(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }


    /**
     * Gets the floating point number from the current node in the database. This number can either be a float or a double depending on what did you put.
     *
     * @param key The key of the value.
     * @return The value that the key holds, or defaultValue if key not exist.
     */
    public double getDecimal(@NonNull String key, double defaultValue) {
        return Double.isNaN(object.optDouble(key)) ? defaultValue : object.optDouble(key);
    }


    /**
     * Gets the POJO associated with the key.
     *
     * @param key   The key of the POJO.
     * @param clazz type of the object that the key contain.
     * @return The Object of class type 'clazz'
     */
    public <T> T getObject(@NonNull String key, Class<T> clazz) {
        JSONObject json = object.optJSONObject(key);
        if (json == null)
            return null;
        else
            return new Gson().fromJson(json.toString(), clazz);
    }


    /**
     * Gets the List of type 'T' associated with the key.
     *
     * @param key  The key of the list.
     * @param type type of items in the list
     * @return The list that the key holds, otherwise an empty list but never null.
     * @throws CloremDatabaseException If the list do-not contains items of type 'T'
     */
    @NonNull
    public <T> List<T> getList(@NonNull String key, Class<T> type) {
        List<T> elements = new ArrayList<>();
        try {
            JSONArray array = object.optJSONArray(key);
            if (array == null)
                return new ArrayList<>();
            else {
                for (int i = 0; i < array.length(); i++) {
                    if (type == String.class || type == int.class || type == boolean.class || type == Number.class){
                        elements.add(type.cast(array.get(i)));
                    } else
                        elements.add(new Gson().fromJson(array.getJSONObject(i).toString(),type));
                }
            }
        } catch (JSONException | ClassCastException e) {
            e.printStackTrace();
            throw new CloremDatabaseException(key + " does not hold a list or of type 'String'", Reasons.REASONS_INVALID_TYPE);
        }
        return elements;
    }

    /**
     * Gets all the data from the current node in the form of map. This map will not contain any nested node.
     *
     * @return The map as data or null if the current node has no data.
     */
    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        for (String key : getChildren()) {
            Object value = object.get(key);
            if (!(value instanceof JSONObject))
                map.put(key, object.get(key));
        }
        return map.size() == 0 ? null : map;
    }

    /**
     * Get the keys of all the children present in this node
     *
     * @return The list of the keys.
     */
    public ArrayList<String> getChildren() {
        ArrayList<String> children = new ArrayList<>();
        Iterator<String> it = object.keys();
        while (it.hasNext()) {
            String child = it.next();
            children.add(child);
        }
        return children;
    }

    /**
     * Gets the path of the current node from database root.
     *
     * @return The path of the current node.
     */
    public String getPath() {
        return path;
    }

    /**
     * Adds a new item to the list. If the key is not present or is not of list, exception will be thrown.
     *
     * @param key   The key of the list.
     * @param value The value to be added.
     * @throws CloremDatabaseException if key is invalid
     */
    public void addItem(@NonNull String key, @NonNull Object value) {
        var array = object.optJSONArray(key);
        if (array == null)
            throw new CloremDatabaseException("There is no list with key " + key + " in this node.", Reasons.REASONS_NOT_EXISTS);
        else
            array.put(value);
    }


    /**
     * Removes the item from the list. If the key is not present or is not of list, exception will be thrown.
     *
     * @param key   The key of the list.
     * @param index The index of the item to be removed.
     * @throws CloremDatabaseException if key is invalid
     */
    public void removeItem(@NonNull String key, int index) {
        try {
            object.getJSONArray(key).remove(index);
        } catch (JSONException e) {
            throw new CloremDatabaseException("The key " + key + " does not hold a list", Reasons.REASONS_INVALID_TYPE);
        }
    }

    /**
     * Use this to run queries on your database. All queries should be run from the parent
     * node which contains many same types of sub-nodes of the child you want to query.
     * For example if you have a node 'users' with many sub-nodes as "userA","userB',"userC"
     * then you have to query from "users" node.
     *
     * @return Query object to run queries.
     */
    public Query query() {
        return new Query(this);
    }

    /**
     * Commit the changes to the database. This method will erase everything from the memory after writing it to database.
     * This should be the last call of database as this will call garbage collector to free up memory which will destroy the current instance of database.
     * This method's runs synchronously.
     */
    public void commit() {
        Clorem.commit(root);
    }
}

