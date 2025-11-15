package models.JSONManagement.Mappers;

import models.exceptions.NullMapperValueException;
import models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CollectionsMapper {
    private static final Map<Class<?>, Class<? extends AbstractMapper<?>>> MAPPER_REGISTRY = new HashMap<>();

    static {
        MAPPER_REGISTRY.put(ContraseniaHash.class, ContraseniaHashMapper.class);
        MAPPER_REGISTRY.put(Cuadricula.class, CuadriculaMapper.class);
        MAPPER_REGISTRY.put(Dibujo.class, DibujoMapper.class);
        MAPPER_REGISTRY.put(UsuarioAdministrador.class, UsuarioAdministradorMapper.class);
        MAPPER_REGISTRY.put(UsuarioNormal.class, UsuarioNormalMapper.class);
    }


    public static JSONObject mapToJSONObject(Map<?, ?> map) throws NullMapperValueException {
        if (map == null) {
            throw new NullMapperValueException("El map ingresado es nulo. Serializacion cancelada");
        }

        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());

            Object value = entry.getValue();
            Object serializedValue = value;

            if (value instanceof Map) {
                serializedValue = mapToJSONObject((Map<?, ?>) value);
            } else if (value instanceof Set) {
                serializedValue = setToJSONArray((Set<?>) value);
            } else if (value != null && isCustomObject(value)) {
                // Si el valor es una instancia de una clase personalizada (clases modelo),
                // DEBE tener un metodo 'objectToJSONObject()' implementado.
                try {
                    serializedValue = value.getClass().getMethod("objectToJSONObject").invoke(value);
                } catch (Exception e) {
                    System.err.println("Error al serializar objeto personalizado: " + e.getMessage());
                    return null;
                }
            }

            jsonObject.put(keyStr, serializedValue);
        }
        return jsonObject;
    }

    public static JSONArray setToJSONArray(Set<?> set) throws NullMapperValueException {
        if (set == null) {
            throw new NullMapperValueException("El set ingresado es nulo. Serializacion cancelada");
        }

        JSONArray jsonArray = new JSONArray();
        for (Object item : set) {
            Object serializedItem = item;

            if (item instanceof Map) {
                serializedItem = mapToJSONObject((Map<?, ?>) item);
            } else if (item instanceof Set) {
                serializedItem = setToJSONArray((Set<?>) item);
            } else if (item != null && isCustomObject(item)) {
                try {
                    serializedItem = item.getClass().getMethod("objectToJSONObject").invoke(item);
                } catch (Exception e) {
                    System.err.println("Error al serializar objeto personalizado: " + e.getMessage());
                }
            }

            jsonArray.put(serializedItem);
        }
        return jsonArray;
    }

    public static <K, V> Map<K, V> jsonObjectToMap(JSONObject jsonObject, Class<K> keyClass, Class<V> valueClass)
            throws NullMapperValueException {

        if (jsonObject == null) {
            throw new NullMapperValueException("El objecto json no puede ser nulo. Error al deserializar a el map");
        }

        Map<K, V> map = new TreeMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String keyStr = keys.next();
            Object value = jsonObject.get(keyStr);

            K key = convertKey(keyStr, keyClass);
            V deserializedValue = convertValue(value, valueClass);

            map.put(key, deserializedValue);
        }
        return map;
    }

    public static <T> Set<T> jsonArrayToSet(JSONArray jsonArray, Class<T> elementClass) throws NullMapperValueException {
        if (jsonArray == null) {
            throw new NullMapperValueException("El arreglo json no puede ser nulo. Error al deserializar a el set");
        }

        Set<T> set = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object item = jsonArray.get(i);

            T deserializedItem = convertValue(item, elementClass);
            set.add(deserializedItem);
        }
        return set;
    }

    private static boolean isCustomObject(Object obj) {
        return !(obj instanceof Number || obj instanceof Boolean || obj instanceof String ||
                obj instanceof Map || obj instanceof Set || obj instanceof java.util.List ||
                obj.getClass().isArray());
    }

    @SuppressWarnings("unchecked")
    private static <K> K convertKey(String keyStr, Class<K> keyClass) {
        if (keyClass == Integer.class || keyClass == int.class) {
            return (K) Integer.valueOf(keyStr);
        } else if (keyClass == Double.class || keyClass == double.class) {
            return (K) Double.valueOf(keyStr);
        }
        return (K) keyStr;
    }

    @SuppressWarnings("unchecked")
    private static <V> V convertValue(Object jsonValue, Class<V> valueClass) throws NullMapperValueException {
        if (jsonValue == null) {
            throw new NullMapperValueException("Valor json nulo. Error en la conversion");
        }

        if (valueClass.isInstance(jsonValue)) {
            return (V) jsonValue;
        }

        switch (jsonValue) {
            case JSONObject jsonObject when Map.class.isAssignableFrom(valueClass) -> {
                return (V) jsonObjectToMap(jsonObject, String.class, Object.class);
            }
            case JSONArray jsonArray when Set.class.isAssignableFrom(valueClass) -> {
                return (V) jsonArrayToSet(jsonArray, Object.class);
            }
            case JSONObject jsonObject -> {
                if (MAPPER_REGISTRY.containsKey(valueClass)) {
                    try {
                        Class<? extends AbstractMapper<V>> mapperClass =
                                (Class<? extends AbstractMapper<V>>) MAPPER_REGISTRY.get(valueClass);
                        AbstractMapper<V> mapper = mapperClass.getConstructor().newInstance();
                        return mapper.jsonObjectToObject(jsonObject);
                    } catch (Exception e) {
                        throw new NullMapperValueException("Error al instanciar o deserializar con Mapper para " + valueClass.getSimpleName() + ": " + e.getMessage());
                    }
                }
            }
            default -> {
                throw new NullMapperValueException("Clase del valor json inidntificable");
            }
        }


        return (V) jsonValue;
    }
}
