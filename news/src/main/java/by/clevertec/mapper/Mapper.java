package by.clevertec.mapper;
public interface Mapper<T, V> {
    V toDTO(T t, V v);

    T fromDTO(V v, T t);
}
