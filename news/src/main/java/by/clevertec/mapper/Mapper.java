package by.clevertec.mapper;
public interface Mapper<T, V> {
    public V toDTO(T t);

    public T fromDTO(V v);
}
